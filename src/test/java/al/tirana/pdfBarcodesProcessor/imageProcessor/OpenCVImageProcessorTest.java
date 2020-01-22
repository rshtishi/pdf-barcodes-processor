package al.tirana.pdfBarcodesProcessor.imageProcessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

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
	 * Testing extraction of bar code images with input being a  image containing a bar code not aligned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test1ExtractBarcodeImages() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test1.png";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		imageProcessor.setBarcodeRatioToImage(0.7, 0.3);
		List<BufferedImage> imageListResult = imageProcessor.extractBarcodeImages(image);
		// verify
		ImageIO.write(imageListResult.get(0), "jpg", new File("src/test/resources/img-barcode-extracted-opencv-1.jpg"));
	}
	
	/**
	 * Testing the extraction  of bar code images with input being an image containing a bar code aligned vertically.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test2ExtractBarcodeImages() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test2.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		imageProcessor.setBarcodeRatioToImage(0.7, 0.3);
		List<BufferedImage>  imageListResult = imageProcessor.extractBarcodeImages(image);
		// verify
		ImageIO.write(imageListResult.get(0), "jpg", new File("src/test/resources/img-barcode-extracted-opencv-2.jpg"));
	}
	
	/**
	 * Testing the extraction of bar code images with input being an image containing a bar code aligned horizontally.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test3ExtractBarcodeImages() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test3.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		imageProcessor.setBarcodeRatioToImage(0.7, 0.3);
		List<BufferedImage> imageListResult = imageProcessor.extractBarcodeImages(image);
		// verify
		ImageIO.write(imageListResult.get(0), "jpg", new File("src/test/resources/img-barcode-extracted-opencv-3.jpg"));		
	}
	
	/**
	 * Testing the extraction of bar code images with input being an image containing small bar code inside document.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test4ExtractBarcodeImages() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test4.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		imageProcessor.setBarcodeRatioToImage(0.9, 0.5);
		List<BufferedImage> imageListResult = imageProcessor.extractBarcodeImages(image);
		// verify
		ImageIO.write(imageListResult.get(0), "jpg", new File("src/test/resources/img-barcode-extracted-opencv-4.jpg"));		
	}
	
	
	/**
	 * Testing the extraction of bar code images with input being an image that has no bar codes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test5ExtractBarcodeImages() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test5.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		imageProcessor.setBarcodeRatioToImage(0.9, 0.5);
		List<BufferedImage> imageListResult = imageProcessor.extractBarcodeImages(image);
		// verify
		int expected = 0;
		assertEquals(expected, imageListResult.size());
	}
	
	/**
	 * Testing the extraction of bar code images with input being an image that has a bar code located in top of document.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test6ExtractBarcodeImages() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test6.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		imageProcessor.setBarcodeRatioToImage(0.45, 0.15);
		List<BufferedImage> imageListResult = imageProcessor.extractBarcodeImages(image);
		// verify
		ImageIO.write(imageListResult.get(0), "jpg", new File("src/test/resources/img-barcode-extracted-opencv-6.jpg"));	
	}
	


	/**
	 * Testing the extraction of bar code image from an image containing a single bar code that is displaced.
	 * code.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test1ExtractBarcodeImage() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test1.png";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		BufferedImage imageResult = imageProcessor.extractBarcodeImage(image);
		// verify
		ImageIO.write(imageResult, "jpg", new File("src/test/resources/img-barcode-extracted1.jpg"));
	}
	
	/**
	 * Testing the extraction of bar code image from an image containing a single bar code that is vertically aligned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test2ExtractBarcodeImage() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test2.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		BufferedImage imageResult = imageProcessor.extractBarcodeImage(image);
		// verify
		ImageIO.write(imageResult, "jpg", new File("src/test/resources/img-barcode-extracted2.jpg"));
	}
	
	/**
	 * Testing the extraction of bar code image from an image containing a single bar code that is horizontally aligned. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void test3ExtractBarcodeImage() throws Exception {
		// setup
		String imagePath = "src/test/resources/img-test3.jpg";
		BufferedImage image = ImageIO.read(new File(imagePath));
		// execute
		BufferedImage imageResult = imageProcessor.extractBarcodeImage(image);
		// verify
		ImageIO.write(imageResult, "jpg", new File("src/test/resources/img-barcode-extracted3.jpg"));
	}
	

	/**
	 * Testing the rotation of an image.
	 * 
	 * @throws Exception
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
