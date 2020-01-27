package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfBoxPdfProcessorTest {

	private PdfBoxPdfProcessor pdfProcessor;

	@Before
	public void setup() {
		pdfProcessor = new PdfBoxPdfProcessor();
	}

	/**
	 * Testing processing of pdf file produces the output we want.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testProcessPdfFile() throws Exception {
		// setup
		String filePath = "src/test/resources/pdf-test1.pdf";
		// execute
		PdfDocument result = pdfProcessor.processPdfFile(filePath);
		// verify
		int totalPagesExpected = 2;
		int pageNumberExpected = 0;
		String pageNameExpected = "pdf-test1#page_0.pdf";
		int totalImagesExpected = 1;
		assertEquals(totalPagesExpected, result.getTotalPages());
		assertEquals(pageNumberExpected, result.getPdfPageList().get(0).getPageNumber());
		assertEquals(pageNameExpected, result.getPdfPageList().get(0).getPageName());
		assertEquals(totalImagesExpected, result.getPdfPageList().get(0).getImages().size());
	}


}
