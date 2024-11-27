import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TessdataDownloader {

    public static void main(String[] args) {
        try {
            String tessdataDir = System.getProperty("user.dir") + "/tessdata";
            File directory = new File(tessdataDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String[] languages = {"eng"}; // Add more languages as needed
            for (String lang : languages) {
                String url = "https://github.com/tesseract-ocr/tessdata/raw/main/" + lang + ".traineddata";
                downloadFile(url, tessdataDir + "/" + lang + ".traineddata");
                System.out.println("Downloaded: " + lang + ".traineddata");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(String urlString, String destination) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        try (BufferedInputStream in = new BufferedInputStream(uri.toURL().openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
             
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);  // Corrected line
            }
        }
    }
}
