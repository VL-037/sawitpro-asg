package client;

import com.recognition.software.jdeskew.ImageDeskew;
import constant.Contstant;
import helper.DirectoryHelper;
import helper.FileHelper;
import helper.OCRHelper;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class TesseractClient {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void extractAndTakeScreenshot() {
        java.io.File folder = new java.io.File(Contstant.IMAGES_DIRECTORY_PATH);
        java.io.File[] listFiles = folder.listFiles();
        DirectoryHelper.createEnhancedImageDirectory();

        WebDriver webDriver = SeleniumClient.initWebDriver();

        for (java.io.File file : listFiles) {
            String mimeType = FileHelper.getMimeType(file);
            if (FileHelper.isImage(mimeType, file)) {
                String ocrResult = extractTextFromImages(file);
                java.io.File outputFile = FileHelper.createOutputFile(file, ocrResult);

                webDriver.switchTo().newWindow(WindowType.TAB);
                SeleniumClient.takeOutputFileScreenshot(webDriver, outputFile);
            }
        }
        webDriver.quit();
    }

    private static String extractTextFromImages(java.io.File file) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(Contstant.TESSDATA_DIRECTORY_PATH);
        tesseract.setLanguage("eng+chi_tra+chi_tra_vert");

        BufferedImage enhancedImage = enhanceImage(file);

        int d = enhancedImage.getRGB(enhancedImage.getTileWidth() / 2, enhancedImage.getTileHeight() / 2);

        String result;
        if (d >= -1.4211511E7 && d < -7254228) {
            result = processImg(tesseract, enhancedImage, 3f, -10f);
        } else if (d >= -7254228 && d < -2171170) {
            result = processImg(tesseract, enhancedImage, 1.455f, -47f);
        } else if (d >= -2171170 && d < -1907998) {
            result = processImg(tesseract, enhancedImage, 1.35f, -10f);
        } else if (d >= -1907998 && d < -257) {
            result = processImg(tesseract, enhancedImage, 1.19f, 0.5f);
        } else if (d >= -257 && d < -1) {
            result = processImg(tesseract, enhancedImage, 1f, 0.5f);
        } else if (d >= -1 && d < 2) {
            result = processImg(tesseract, enhancedImage, 1f, 0.35f);
        } else {
            return doOCR(tesseract, enhancedImage);
        }
        return result;
    }

    private static BufferedImage enhanceImage(java.io.File file) {
        BufferedImage rawImage = FileHelper.readRawImage(file);

        ImageDeskew imageDeskew = new ImageDeskew(rawImage);
        BufferedImage rotatedImage = ImageHelper.rotateImage(rawImage, imageDeskew.getSkewAngle());

        int width = rotatedImage.getWidth();
        int height = rotatedImage.getHeight();

        BufferedImage scaledImage = ImageHelper
                .getScaledInstance(rotatedImage, width * 2, height * 2);

        BufferedImage grayScaledImage = ImageHelper.convertImageToGrayscale(scaledImage);

        Mat thresholdImageMat = OCRHelper.thresholding(grayScaledImage);

        String enhancedImagePath = Contstant.ENHANCED_IMAGES_DIRECTORY_ABSOLUTE_PATH + file.getName();
        Imgcodecs.imwrite(enhancedImagePath, thresholdImageMat);

        return FileHelper.matToBufferImage(thresholdImageMat);
    }

    private static String processImg(Tesseract tesseract, BufferedImage inputImage, float scaleFactor, float offset) {
        BufferedImage outputImage = new BufferedImage(1050, 1024, inputImage.getType());

        Graphics2D graphic = outputImage.createGraphics();
        graphic.drawImage(inputImage, 0, 0, 1050, 1024, null);
        graphic.dispose();

        RescaleOp rescaleOutput = new RescaleOp(scaleFactor, offset, null);
        BufferedImage finalOutputImage = rescaleOutput.filter(outputImage, null);

        return doOCR(tesseract, finalOutputImage);
    }

    private static String doOCR(Tesseract tesseract, BufferedImage image) {
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            System.err.printf("#doOCR ERROR! finalOutputImage: %s, errorMessage: %s", image, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
