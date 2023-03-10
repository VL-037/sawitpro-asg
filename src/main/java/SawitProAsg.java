import client.GoogleClient;
import client.TesseractClient;
import constant.Contstant;
import helper.DirectoryHelper;

import java.io.InputStream;

public class SawitProAsg {
    public static void main(String... args) {
        InputStream credentialsStream = SawitProAsg.class.getResourceAsStream(Contstant.CREDENTIALS_FILE_PATH);

        DirectoryHelper.deleteDirectories();

        GoogleClient.createGDrive(credentialsStream);
        TesseractClient.extractTextAndCreateOutput();
        TesseractClient.screenshotOutput();
    }
}