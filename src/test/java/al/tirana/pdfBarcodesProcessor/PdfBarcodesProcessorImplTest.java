package al.tirana.pdfBarcodesProcessor;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfDocument;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfBarcodesProcessorImplTest {

	PdfBarcodesProcessor pdfBarcodesProcessor;

	@Before
	public void setup() {
		pdfBarcodesProcessor = new PdfBarcodesProcessorImpl.Builder().build();
	}

	@Test
	public void testProcessPdfWithSingleBarcodePerPage() throws Exception {
		// setup
		String filePath = "src/test/resources/pdf-test1.pdf";
		// execute
		PdfDocument resultDoc = pdfBarcodesProcessor.processPdfBarcodesPerPage(filePath);
		// verify
		String expectedPage1 = "100000000003";
		String expectedPage2 = "9109342584073898002361401267";
		assertTrue(resultDoc.getPdfPageList().get(0).getDecodedBarcodeImageMap().keySet().contains(expectedPage1));
		assertTrue(resultDoc.getPdfPageList().get(1).getDecodedBarcodeImageMap().keySet().contains(expectedPage2));
	}

}
