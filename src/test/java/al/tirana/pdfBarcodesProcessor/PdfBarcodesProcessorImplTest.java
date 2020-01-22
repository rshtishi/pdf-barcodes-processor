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
		//execute
		PdfDocument resultDoc = pdfBarcodesProcessor.processPdfBarcodesPerPage(filePath);
		//verify
		resultDoc.getPdfPageList().get(2).getDecodedBarcodeImageMap().keySet().forEach(System.out::println);
	}

}
