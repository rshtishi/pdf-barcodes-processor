package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PdfBoxAdapterTest {
	
	private PdfBoxAdapter pdfProcessor;
	
	@Before
	public void setup() {
		pdfProcessor = new PdfBoxAdapter();
	}

	@Test
	public void testProcessPdfFile() throws Exception {
		//setup
		String filePath = "src/test/resources/pdf-test2.pdf";
		//execute
		PdfDocument result = pdfProcessor.processPdfFile(filePath);
		//verify
		int totalPagesExpected = 1;
		int pageNumberExpected = 0;
		String pageNameExpected = "pdf-test2#page_0.pdf";
		int totalImagesExpected = 1;
		assertEquals(result.getTotalPages(),totalPagesExpected);
		assertEquals(result.getPdfPageList().get(0).getPageNumber(),pageNumberExpected);
		assertEquals(result.getPdfPageList().get(0).getPageName(),pageNameExpected);
		assertEquals(result.getPdfPageList().get(0).getImages().size(),totalImagesExpected);
		
	}

}
