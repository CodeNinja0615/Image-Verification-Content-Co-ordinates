import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ImageVerification {

    public static void main(String[] args) throws Exception {	
    	File largeImageFile = new File("C:/SeleniumFramework/Images/CapturedImages/screenshot.bmp");
    	File smallImageFile = new File("C:/SeleniumFramework/Images/Icon/GOTG.bmp");

        BufferedImage largeImage = ImageIO.read(largeImageFile);
        BufferedImage smallImage = ImageIO.read(smallImageFile);

        // Convert both images to grayscale
        BufferedImage grayLargeImage = convertToGrayscale(largeImage);
        BufferedImage graySmallImage = convertToGrayscale(smallImage);

        Point coordinates = findImageCoordinates(grayLargeImage, graySmallImage);
        if (coordinates != null) {
            System.out.println("Small image found in the large image.");
            System.out.println("Small image found at coordinates: " + coordinates);
        } else {
            System.out.println("Small image not found in the large image.");
        }
    }

    // Method to convert image to grayscale
    public static BufferedImage convertToGrayscale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = grayImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return grayImage;
    }

    // Method to find coordinates of the small image in the large image
    public static Point findImageCoordinates(BufferedImage largeImage, BufferedImage smallImage) throws InterruptedException, ExecutionException {
        int width = largeImage.getWidth();
        int height = largeImage.getHeight();
        int smallWidth = smallImage.getWidth();
        int smallHeight = smallImage.getHeight();

        ExecutorService executor = Executors.newFixedThreadPool(4); // Create a thread pool for optimization
        List<Callable<Point>> tasks = new ArrayList<>();

        // Loop through all possible positions where the small image can fit inside the large image
        for (int y = 0; y <= height - smallHeight; y++) {
            for (int x = 0; x <= width - smallWidth; x++) {
                int finalX = x;
                int finalY = y;
                tasks.add(() -> compareRegion(largeImage, smallImage, finalX, finalY));
            }
        }

        List<Future<Point>> results = executor.invokeAll(tasks);
        executor.shutdown();

        // Check if any task returned coordinates
        for (Future<Point> result : results) {
            Point coordinates = result.get();
            if (coordinates != null) {
                return coordinates; // Found a match
            }
        }

        return null; // No match found
    }

    // Method to compare one region of the large image with the small image and return coordinates if matched
    public static Point compareRegion(BufferedImage largeImage, BufferedImage smallImage, int startX, int startY) {
        int smallWidth = smallImage.getWidth();
        int smallHeight = smallImage.getHeight();
        int tolerance = 10; // Tolerance for pixel value variations

        // Compare each pixel in the small image with the corresponding pixel in the large image
        for (int i = 0; i < smallWidth; i++) {
            for (int j = 0; j < smallHeight; j++) {
                int largePixel = largeImage.getRGB(startX + i, startY + j);
                int smallPixel = smallImage.getRGB(i, j);

                // Extract grayscale values
                int largeGray = (largePixel >> 16) & 0xff; // R, G, B are the same in grayscale
                int smallGray = (smallPixel >> 16) & 0xff;

                // Compare grayscale values with a tolerance level
                if (Math.abs(largeGray - smallGray) > tolerance) {
                    return null; // Mismatch found
                }
            }
        }

        return new Point(startX, startY); // Return the top-left corner coordinates where the match was found
    }
}
