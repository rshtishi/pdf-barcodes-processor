package al.tirana.pdfBarcodesProcessor;

import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfDocument;

/**
 * 
 * @author Rando Shtishi
 *
 */
public interface PdfBarcodesProcessor {
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * Process the pdf file that have 0-n barcodes per page and
	 * produce a list with decoded barcodes per each page
	 */
	PdfDocument processPdfWithMultipleBarcodesPerPage(String filePath);
	
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 * Process the pdf file that have 0-1 barcode per page
	 * and produces a list with decoded barcodes per each page.  
	 */
	PdfDocument processPdfWithSingleBarcodePerPage(String filePath) throws Exception;

}
