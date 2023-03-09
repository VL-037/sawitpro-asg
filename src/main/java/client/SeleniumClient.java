package client;

import constant.Contstant;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Core;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

public class SeleniumClient {

    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chromedriver.exe");
    }

    public static void takeOutputFileScreenshot(WebDriver webDriver, java.io.File file) {
        webDriver.get(file.getAbsolutePath());
        java.io.File screenshotFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

        String screenshotFileName =
                FilenameUtils.removeExtension(file.getName()) + "." + FilenameUtils.getExtension(screenshotFile.getName());
        try {
            FileUtils.copyFile(screenshotFile,
                    new java.io.File(Contstant.OCR_OUTPUT_SCREENSHOT_DIRECTORY_PATH + screenshotFileName));
        } catch (IOException e) {
            System.err.printf("#takeOutputFileScreenshot ERROR! webDriver: %s, file: %s, errorMessage: %s", webDriver, file, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static WebDriver initWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();

        return webDriver;
    }
}
