package constant;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.util.ArrayList;
import java.util.List;

public class Contstant {
    public static final String APPLICATION_NAME = "SawitPro Assignment";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    public static final String RESOURCES_DIRECTORY_PATH = "src/main/resources/";
    public static final String IMAGES_DIRECTORY_PATH = RESOURCES_DIRECTORY_PATH + "files/";
    public static final String ENHANCED_IMAGES_DIRECTORY_PATH = IMAGES_DIRECTORY_PATH + "enhanced/";
    public static final String ENHANCED_IMAGES_DIRECTORY_ABSOLUTE_PATH = System.getProperty("user.dir") + "/" + ENHANCED_IMAGES_DIRECTORY_PATH;

    public static final String TEMPLATE_DIRECTORY_PATH = RESOURCES_DIRECTORY_PATH + "template/";
    public static final String IMAGE_OCR_OUTPUT_DIRECTORY_PATH = IMAGES_DIRECTORY_PATH + "output/";
    public static final String OCR_OUTPUT_SCREENSHOT_DIRECTORY_PATH = IMAGE_OCR_OUTPUT_DIRECTORY_PATH + "screenshot/";
    public static final String TESSDATA_DIRECTORY_PATH = "C:\\Program Files\\Tesseract-OCR\\tessdata\\";
}
