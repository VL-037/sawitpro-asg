package helper;

import constant.Contstant;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryHelper {

    public static void deleteDirectories() {
        try {
            FileUtils.deleteDirectory(new java.io.File(Contstant.ENHANCED_IMAGES_DIRECTORY_PATH));
            FileUtils.deleteDirectory(new java.io.File(Contstant.IMAGE_OCR_OUTPUT_DIRECTORY_PATH));
        } catch (IOException e) {
            System.err.printf("#deleteDirectories ERROR! errorMessage: %s", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void createEnhancedImageDirectory() {
        String enhancedImagesPath = System.getProperty("user.dir") + "/" + Contstant.ENHANCED_IMAGES_DIRECTORY_PATH;
        try {
            Files.createDirectories(Paths.get(enhancedImagesPath));
        } catch (IOException e) {
            System.err.printf("#createEnhancedImageDirectory ERROR! errorMessage: %s", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
