package al.tirana.pdfBarcodesProcessor;

import java.io.File;
import java.io.FileNotFoundException;

public class PdfBarcodesProcessor {
	
	public void processPdfFile(String fileStr) throws Exception{
		File file = new File(fileStr);
		if(file.exists()){
			//TO DO
		} else {
			throw new FileNotFoundException("File not found ");
		}
	}

}
