package helper;

import constant.Contstant;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

public class OCRHelper {

    public static Mat tesseractThresholding(BufferedImage image) {
        Mat imageMat = FileHelper.bufferedImageToMat(image);
        Mat thresholdImageMat = new Mat();
        Imgproc.threshold(imageMat, thresholdImageMat, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        return thresholdImageMat;
    }

    public static String setOCROutput(java.io.File file) {
        return Contstant.IMAGE_OCR_OUTPUT_DIRECTORY_PATH + FilenameUtils.removeExtension(file.getName()) + ".html";
    }
}
