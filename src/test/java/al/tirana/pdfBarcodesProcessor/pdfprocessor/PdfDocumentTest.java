package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

public class PdfDocumentTest {

	@Test
	public void testSaveAllDecodedBarcodeImages() throws IOException {
		// setup
		PdfPage pdfPage = new PdfPage.Builder().build();
		BufferedImage image = ImageIO.read(new File("src/test/resources/img-test1.jpg"));
		pdfPage.getDecodedBarcodeImageMap().put("testAll", image);
		List<PdfPage> pdfPages = new ArrayList<>();
		pdfPages.add(pdfPage);
		PdfDocument pdfDocument = new PdfDocument.Builder().pdfPages(pdfPages).build();
		// execute
		pdfDocument.saveAllDecodedBarcodeImages("src/test/resources");
		// verify
		File file = new File("src/test/resources/testAll.jpg");
		assertTrue(file.exists());
	}

}
