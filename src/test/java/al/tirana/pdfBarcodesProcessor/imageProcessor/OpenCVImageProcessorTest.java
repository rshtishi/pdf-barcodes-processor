package al.tirana.pdfBarcodesProcessor.imageProcessor;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class OpenCVImageProcessorTest {

	private OpenCVImageProcessor imageProcessor;

	@Before
	public void setup() {
		imageProcessor = new OpenCVImageProcessor();
	}

	@Test
	public void testExtractBarcodeImage() throws Exception {
		// setup
		String imagePath1 = "src/test/resources/img-test1.png";
		BufferedImage image1 = ImageIO.read(new File(imagePath1));
		String imagePath2 = "src/test/resources/img-test2.png";
		BufferedImage image2 = ImageIO.read(new File(imagePath2));
		String imagePath3 = "src/test/resources/img-test3.jpg";
		BufferedImage image3 = ImageIO.read(new File(imagePath3));
		String imagePath4 = "src/test/resources/img-test4.png";
		BufferedImage image4 = ImageIO.read(new File(imagePath4));
		// execute
		BufferedImage imageResult1 = imageProcessor.extractBarcodeImage(image1);
		BufferedImage imageResult2 = imageProcessor.extractBarcodeImage(image2);
		BufferedImage imageResult3 = imageProcessor.extractBarcodeImage(image3);
		BufferedImage imageResult4 = imageProcessor.extractBarcodeImage(image4);
		// verify
		ImageIO.write(imageResult1, "jpg", new File("src/test/resources/img-barcode-extracted1.jpg"));
		assertEquals(imageResult1.getWidth(), 411);
		assertEquals(imageResult1.getHeight(), 328);
		ImageIO.write(imageResult2, "jpg", new File("src/test/resources/img-barcode-extracted2.jpg"));
		assertEquals(imageResult2.getWidth(), 628);
		assertEquals(imageResult2.getHeight(), 139);
		ImageIO.write(imageResult3, "jpg", new File("src/test/resources/img-barcode-extracted3.jpg"));
		assertEquals(imageResult3.getWidth(), 258);
		assertEquals(imageResult3.getHeight(), 84);
		ImageIO.write(imageResult4, "jpg", new File("src/test/resources/img-barcode-extracted4.jpg"));
		assertEquals(imageResult4.getWidth(), 973);
		assertEquals(imageResult4.getHeight(), 243);
	}

	@Test
	public void testDetectBarcodeImageUsingClassifier() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test5.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		Method method = imageProcessor.getClass().getDeclaredMethod("detectBarcodeImageUsingClassifier",
				java.awt.image.BufferedImage.class);
		method.setAccessible(true);
		BufferedImage imageResult = (BufferedImage) method.invoke(imageProcessor, image);
		// verify
		ImageIO.write(imageResult, "jpg", new File("src/test/resources/img-barcode-detected.jpg"));
	}

	@Test
	public void testRotateImage() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test2.png";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		BufferedImage imageResult = imageProcessor.rotateImage(image, 180);
		// verify
		ImageIO.write(imageResult, "jpg", new File("src/test/resources/img-rotated.jpg"));
		assertEquals(image.getWidth(), imageResult.getWidth());
		assertEquals(image.getHeight(), imageResult.getHeight());
	}

}
