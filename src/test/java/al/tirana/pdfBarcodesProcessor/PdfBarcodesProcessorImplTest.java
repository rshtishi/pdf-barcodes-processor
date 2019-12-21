package al.tirana.pdfBarcodesProcessor;

import static org.junit.Assert.assertEquals;

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
		pdfBarcodesProcessor = new PdfBarcodesProcessorImpl();
	}
	
	@Test
	public void testProcessPdfWithSingleBarcodePerPage() throws Exception {
		//setup
		String filePath = "src/test/resources/pdf-test1.pdf";
		String filePath2 = "src/test/resources/pdf-test2.pdf";
		//execute
		PdfDocument resultDoc = pdfBarcodesProcessor.processPdfWithSingleBarcodePerPage(filePath);
		PdfDocument resultDoc2 = pdfBarcodesProcessor.processPdfWithSingleBarcodePerPage(filePath2);
		//verify
		String expected = "DCW-00000002";
		assertEquals(resultDoc.getPdfPageList().get(0).getDecodedBarcodes().get(0),expected);
		//assertEquals(resultDoc2.getPdfPageList().get(0).getDecodedBarcodes().get(0),expected);
		System.out.println("Result: "+resultDoc2.getPdfPageList().get(0).getDecodedBarcodes().get(0));
	}

}
