package al.tirana.pdfBarcodesProcessor.pdfprocessor;

public interface PdfProcessor {
	
	public PdfDocument processPdfFile(String filePath) throws Exception;

}
