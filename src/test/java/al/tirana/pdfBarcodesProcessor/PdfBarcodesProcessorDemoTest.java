package al.tirana.pdfBarcodesProcessor;

import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfBarcodesProcessorDemoTest {
	
	PdfBarcodesProcessorDemo pdfBarcodesProcessor;
	
	@Before
	public void setup(){
		pdfBarcodesProcessor = new PdfBarcodesProcessorDemo();
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
