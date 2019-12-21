package al.tirana.pdfBarcodesProcessor;

import org.junit.Before;
import org.junit.Test;

public class PdfBarcodesProcessorTest {
	
	PdfBarcodesProcessor pdfBarcodesProcessor;
	
	@Before
	public void setup(){
		pdfBarcodesProcessor = new PdfBarcodesProcessor();
	}
	
	@Test
	public void test() throws Exception {
		//setup
		String filePath = "src/test/resources/pdf-test2.pdf";
		//execute
		pdfBarcodesProcessor.processPdfFile(filePath);
		//verify
	}

}
