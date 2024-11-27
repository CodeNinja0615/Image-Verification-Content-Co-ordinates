import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OCRUtility {

	public static void main(String[] args) throws Exception {	
    	String imagePath = "C:/SeleniumFramework/Images/CapturedImages/CapturedImg2.bmp";
    	System.out.println(extractText(imagePath,726, 324, 256, 27));
	}
    public static String extractText(String imagePath, int x, int y, int width, int height) throws IOException, TesseractException {
        // Load the image
        BufferedImage image = ImageIO.read(new File(imagePath));

        // Crop the desired region
        BufferedImage croppedImage = image.getSubimage(x, y, width, height);

        // Set up Tesseract OCR
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(System.getProperty("user.dir") + "/tessdata"); // Dynamic tessdata path
        tesseract.setLanguage("eng"); // Specify language

        // Perform OCR
        return tesseract.doOCR(croppedImage);
    }
}
