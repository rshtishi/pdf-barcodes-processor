package al.tirana.pdfBarcodesProcessor.imageProcessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import al.tirana.pdfBarcodesProcessor.barcodeDecoder.ZxingBarcodeDecoder;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class OpenCVImageProcessorTest {

	private OpenCVImageProcessor imageProcessor;
	private ZxingBarcodeDecoder decoder;

	@Before
	public void setup() {
		imageProcessor = new OpenCVImageProcessor();
		decoder = new ZxingBarcodeDecoder();
	}

	/**
	 * @throws Exception Testing extraction bar code images from image containing
	 *                   multiple bar codes. Not Finished.
	 */
	@Test
	public void testExtractBarcodeImages() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test5.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		List<BufferedImage> imageListResult = imageProcessor.extractBarcodeImages(image);
		// verify
		ImageIO.write(imageListResult.get(0), "jpg", new File("src/test/resources/img-barcode-detected.jpg"));
	}

	/**
	 * @throws Exception Testing the extraction a bar code image from image
	 *                   containing a single bar code.
	 */
	@Test
	public void testExtractBarcodeImage() throws Exception {
		// setup
		String imagePath1 = "src/test/resources/img-test1.png";
		BufferedImage image1 = ImageIO.read(new File(imagePath1));
		String imagePath3 = "src/test/resources/img-test3.jpg";
		BufferedImage image3 = ImageIO.read(new File(imagePath3));
		String imagePath4 = "src/test/resources/img-test4.png";
		BufferedImage image4 = ImageIO.read(new File(imagePath4));
		String imagePath5 = "src/test/resources/img-test5.jpg";
		BufferedImage image5 = ImageIO.read(new File(imagePath5));
		// execute
		BarcodeImage imageResult1 = imageProcessor.extractBarcodeImage(image1);
		BarcodeImage imageResult3 = imageProcessor.extractBarcodeImage(image3);
		BarcodeImage imageResult4 = imageProcessor.extractBarcodeImage(image4);
		BarcodeImage imageResult5 = imageProcessor.extractBarcodeImage(image5);
		// verify
		ImageIO.write(imageResult1.getImage(), "jpg", new File("src/test/resources/img-barcode-extracted1.jpg"));
		assertEquals(imageResult1.getImage().getWidth(), 411);
		assertEquals(imageResult1.getImage().getHeight(), 328);
		assertTrue(imageResult1.isSkewed());
		ImageIO.write(imageResult3.getImage(), "jpg", new File("src/test/resources/img-barcode-extracted3.jpg"));
		assertEquals(imageResult3.getImage().getWidth(), 258);
		assertEquals(imageResult3.getImage().getHeight(), 84);
		assertFalse(imageResult3.isSkewed());
		ImageIO.write(imageResult4.getImage(), "jpg", new File("src/test/resources/img-barcode-extracted4.jpg"));
		assertEquals(imageResult4.getImage().getWidth(), 973);
		assertEquals(imageResult4.getImage().getHeight(), 243);
		assertTrue(imageResult4.isSkewed());
		ImageIO.write(imageResult5.getImage(), "jpg", new File("src/test/resources/img-barcode-extracted5.jpg"));
		assertFalse(imageResult5.isSkewed());
	}

	/**
	 * @throws Exception Testing the rotating of image.
	 */
	@Test
	public void testRotateImage() throws Exception {
		// setup
		String imagePath1 = "src/test/resources/img-test1.png";
		String imagePath2 = "src/test/resources/img-test2.jpg";
		BufferedImage image1 = ImageIO.read(new File(imagePath1));
		BufferedImage image2 = ImageIO.read(new File(imagePath2));
		// execute
		BufferedImage imageResult1 = imageProcessor.rotateImage(image1, 50);
		BufferedImage imageResult2 = imageProcessor.rotateImage(image2, 100);
		// verify
		ImageIO.write(imageResult1, "jpg", new File("src/test/resources/img-rotated-50.jpg"));
		String expected = "B121839";
		assertEquals(expected, decoder.decode(imageResult1));
		ImageIO.write(imageResult2, "jpg", new File("src/test/resources/img-rotated-100.jpg"));
		expected = "Seite5";
		assertEquals(expected, decoder.decode(imageResult2));
	}

}
