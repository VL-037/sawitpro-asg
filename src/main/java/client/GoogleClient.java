package client;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import constant.Contstant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleClient {

    public static void createGDrive(InputStream inputStream) {
        final NetHttpTransport HTTP_TRANSPORT = getHttpTransport();
        Credential credential = getCredential(HTTP_TRANSPORT, inputStream);
        Drive service = new Drive.Builder(HTTP_TRANSPORT, Contstant.JSON_FACTORY, credential)
                .setApplicationName(Contstant.APPLICATION_NAME)
                .build();

        String folderId = createGoogleDriveFolder(service);
        uploadImagesToGoogleDrive(service, folderId);
    }

    private static NetHttpTransport getHttpTransport() {
        try {
            return GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            System.err.printf("#deleteDirectories GeneralSecurityException ERROR! errorMessage: %s", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.printf("#deleteDirectories IOException ERROR! errorMessage: %s", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static Credential getCredential(final NetHttpTransport HTTP_TRANSPORT, InputStream inputStream) {
        try {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + Contstant.CREDENTIALS_FILE_PATH);
            }
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(Contstant.JSON_FACTORY, new InputStreamReader(inputStream));

            List<String> scopes = new ArrayList<>();
            scopes.add(DriveScopes.DRIVE);
            scopes.add(DriveScopes.DRIVE_FILE);
            scopes.add(DriveScopes.DRIVE_APPDATA);

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                    .Builder(HTTP_TRANSPORT, Contstant.JSON_FACTORY, clientSecrets, scopes)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(Contstant.TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                    .setPort(8080)
                    .build();

            return new AuthorizationCodeInstalledApp(flow, receiver)
                    .authorize("user");
        } catch (IOException e) {
            System.err.printf("#getCredential ERROR! errorMessage: %s", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static String createGoogleDriveFolder(Drive service) {
        File gDriveFolder = new File();
        gDriveFolder.setName(service.getApplicationName());
        gDriveFolder.setMimeType("application/vnd.google-apps.folder");
        try {
            File uploadedFolder = service.files()
                    .create(gDriveFolder)
                    .setFields("id")
                    .execute();
            System.out.println("Folder created: " + uploadedFolder);

            return uploadedFolder.getId();
        } catch (IOException e) {
            System.err.printf("#createGoogleDriveFolder ERROR! service: %s, errorMessage: %s", service, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static void uploadImagesToGoogleDrive(Drive service, String folderId) {
        try {
            java.io.File folder = new java.io.File(Contstant.IMAGES_DIRECTORY_PATH);
            java.io.File[] listFiles = folder.listFiles();

            for (java.io.File file : listFiles) {
                File gdriveFile = new File();
                gdriveFile.setName(file.getName());
                gdriveFile.setParents(Collections.singletonList(folderId));

                FileContent mediaContent = new FileContent("image/jpg", file);

                File uploadedFile = service.files()
                        .create(gdriveFile, mediaContent)
                        .setFields("id")
                        .execute();
                System.out.println("File uploaded: " + uploadedFile);
            }
        } catch (IOException e) {
            System.err.printf("#uploadImagesToGoogleDrive ERROR! service: %s, folderId: %s, errorMessage: %s", service, folderId, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
