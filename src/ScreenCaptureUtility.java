import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenCaptureUtility {

    public static void main(String[] args) throws InterruptedException {
        try {
        	Thread.sleep(5000);
            // Capture the screenshot of the entire screen
            BufferedImage screenshot = captureScreen();

            // Save the screenshot as a BMP file
            String path = "C:/SeleniumFramework/Images/CapturedImages/";
            File outputFile = new File(path+"screenshot.bmp");
            ImageIO.write(screenshot, "BMP", outputFile);
            System.out.println("Screenshot captured and saved as screenshot.bmp");

        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    // Method to capture the entire screen
    public static BufferedImage captureScreen() throws AWTException {
        // Create a Robot object to capture screen
        Robot robot = new Robot();

        // Get the screen size (full screen size)
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        // Capture the screenshot of the entire screen
        BufferedImage screenshot = robot.createScreenCapture(screenRect);

        return screenshot;
    }
}
