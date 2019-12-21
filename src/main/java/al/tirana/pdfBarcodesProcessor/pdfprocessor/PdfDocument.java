package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import java.util.List;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfDocument {

	private int totalPages;
	private List<PdfPage> pdfPageList;

	private PdfDocument() {
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<PdfPage> getPdfPageList() {
		return pdfPageList;
	}

	public static class Builder {

		private int totalPages;
		private List<PdfPage> pdfPageList;

		public Builder() {
		}

		public Builder totalPages(int totalPages) {
			this.totalPages = totalPages;
			return this;
		}

		public Builder pdfPages(List<PdfPage> pdfPageList) {
			this.pdfPageList = pdfPageList;
			return this;
		}

		public PdfDocument build() {
			PdfDocument pdfDocument = new PdfDocument();
			pdfDocument.totalPages = this.totalPages;
			pdfDocument.pdfPageList = this.pdfPageList;
			return pdfDocument;
		}

	}

}
