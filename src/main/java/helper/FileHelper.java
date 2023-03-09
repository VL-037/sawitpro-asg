package helper;

import constant.Contstant;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static helper.OCRHelper.setOCROutput;

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

    public static java.io.File createOutputFile(java.io.File file, String result) {
        try {
            result = StringHelper.applyColor(result);

            StringBuilder contentBuilder = new StringBuilder();
            BufferedReader htmlInput = new BufferedReader(new FileReader(Contstant.TEMPLATE_DIRECTORY_PATH + "template.html"));
            String htmlString;
            while ((htmlString = htmlInput.readLine()) != null) {
                contentBuilder.append(htmlString);
            }
            htmlString = contentBuilder.toString();
            htmlString = htmlString.replace("$title", file.getName());

            result = result.replaceAll("(\r\n|\n)", "<br />");
            htmlString = htmlString.replace("$body", result);

            java.io.File newHtmlFile = new java.io.File(setOCROutput(file));
            FileUtils.writeStringToFile(newHtmlFile, htmlString);

            System.out.println("Text from Image: " + file.getName());
            System.out.println("=========================");
            System.out.println(result);
            return newHtmlFile;
        } catch (IOException e) {
            System.err.printf("#createOutputFile ERROR! file: %s, result: %s, errorMessage: %s", file, result, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
