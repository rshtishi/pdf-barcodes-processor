package al.tirana.pdfBarcodesProcessor;

import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfDocument;

/**
 * 
 * @author Rando Shtishi
 *
 */
public interface PdfBarcodesProcessor {

	/**
	 * Process the pdf file that have 0-n barcodes per page and produce a list with
	 * decoded barcodes per each page
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	PdfDocument processPdfBarcodesPerPage(String filePath) throws Exception;

}
