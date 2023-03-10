package helper;

import constant.Contstant;
import constant.Language;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class FileHelper {

    public static String getMimeType(java.io.File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            System.err.printf("#getMimeType ERROR! file: %s, errorMessage: %s", file, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static boolean isImage(String mimeType, java.io.File file) {
        return Objects.nonNull(mimeType)
                && getMimeType(file).split("/")[0].equals("image");
    }

    public static Mat bufferedImageToMat(BufferedImage image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            System.err.printf("#bufferedImageToMat ERROR! image: %s, errorMessage: %s", image, e.getMessage());
            throw new RuntimeException(e);
        }
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
    }

    public static BufferedImage matToBufferImage(Mat matrix) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        try {
            return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        } catch (IOException e) {
            System.err.printf("#matToBufferImage ERROR! matrix: %s, errorMessage: %s", matrix, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage readRawImage(java.io.File file) {
        try {
            BufferedImage rawImage = ImageIO.read(file);
            return rawImage;
        } catch (IOException e) {
            System.err.printf("#readRawImage ERROR! file: %s, errorMessage: %s", file, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static List<File> createOutputFile(java.io.File file, String result) {
        List<File> files = new ArrayList<>();
        Map<Language, String> languageStringMap = getLanguageStringMap(result);

        languageStringMap.forEach((language, text) -> {
            text = StringHelper.convertToUnicode(text);
            if (language.equals(Language.ENG)) {
                text = StringHelper.applyColor(text);
            }

            try {
                StringBuilder contentBuilder = new StringBuilder();
                BufferedReader htmlInput = new BufferedReader(new FileReader(Contstant.TEMPLATE_DIRECTORY_PATH + "template.html"));

                String htmlString;
                while ((htmlString = htmlInput.readLine()) != null) {
                    contentBuilder.append(htmlString);
                }
                htmlString = contentBuilder.toString();

                String fileTitle = FilenameUtils.removeExtension(file.getName())
                        + "_"
                        + language.getLabel();
                htmlString = htmlString.replace("$title", fileTitle);

                text = text.replaceAll("(\r\n|\n)", "<br />");
                htmlString = htmlString.replace("$body", text);

                java.io.File newHtmlFile = new java.io.File(OCRHelper.setOCROutput(file, fileTitle));
                FileUtils.writeStringToFile(newHtmlFile, htmlString);

                files.add(newHtmlFile);

                System.out.println("Text from Image " + language.getLabel() + ": " + file.getName());
                System.out.println("=========================");
                System.out.println(text);
            } catch (IOException e) {
                System.err.printf("#createOutputFile ERROR! file: %s, result: %s, errorMessage: %s", file, text, e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
        return files;
    }

    private static Map<Language, String> getLanguageStringMap(String str) {
        Map<Language, String> languageStringMap = new HashMap<>();
        Language[] languages = {Language.CHI, Language.ENG};

        for (Language language : languages) {
            if (!language.equals(Language.ENG)) {
                Pattern pattern = Pattern.compile(language.getRegex());
                String foreignString = StringHelper.extractLanguageCharacters(str, pattern);

                languageStringMap.put(language, foreignString);
            }
        }
        String englishString = StringHelper.extractEnglishLanguageCharacters(str, languageStringMap);
        languageStringMap.put(Language.ENG, englishString);

        return languageStringMap;
    }
}
