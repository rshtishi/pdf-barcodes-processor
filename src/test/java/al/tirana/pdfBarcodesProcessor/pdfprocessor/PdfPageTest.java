package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class PdfPageTest {

	private PdfPage pdfPage;

	@Before()
	public void setup() throws IOException {
		pdfPage = new PdfPage.Builder().build();
		BufferedImage image = ImageIO.read(new File("src/test/resources/img-test1.jpg"));
		pdfPage.getDecodedBarcodeImageMap().put("test", image);
	}

	@Test
	public void testSaveDecodedBarcodeImages() {
		// execute
		pdfPage.saveDecodedBarcodeImages("src/test/resources");
		// verify
		File file = new File("src/test/resources/test.jpg");
		assertTrue(file.exists());
	}

	@Test
	public void testGetAllDecodedBarcodeImages() {
		// execute
		List<BufferedImage> images = pdfPage.getAllDecodedBarcodeImages();
		// verify
		int expected = 1;
		assertEquals(expected, images.size());
	}

	@Test
	public void testGetAllDecodedBarcodesList() {
		// execute
		List<BufferedImage> decodedBarcodes = pdfPage.getAllDecodedBarcodeImages();
		// verify
		int expected = 1;
		assertEquals(expected, decodedBarcodes.size());
	}

}
