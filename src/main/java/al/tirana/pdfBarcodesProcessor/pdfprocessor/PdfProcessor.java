package al.tirana.pdfBarcodesProcessor.pdfprocessor;

/**
 * 
 * @author Rando Shtishi
 *
 */
public interface PdfProcessor {

	/**
	 * Processes a pdf and produces a document that contains a number of pages equal
	 * to number page that pdf document has. Each page of document has a list of
	 * images that are in pdf page.
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public PdfDocument processPdfFile(String filePath) throws Exception;

}
