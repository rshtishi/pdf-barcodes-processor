package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfPage {

	private int pageNumber;
	private List<BufferedImage> images;
	private String pageName;
	private List<String> decodedBarcodes;

	public List<String> getDecodedBarcodes() {
		return decodedBarcodes;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public List<BufferedImage> getImages() {
		return images;
	}

	public String getPageName() {
		return pageName;
	}

	public static class Builder {

		private int pageNumber;
		private List<BufferedImage> images;
		private String pageName;

		public Builder() {
		}

		public Builder pageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
			return this;
		}

		public Builder pageName(String pageName) {
			this.pageName = pageName;
			return this;
		}

		public Builder images(List<BufferedImage> images) {
			this.images = images;
			return this;
		}

		public PdfPage build() {
			PdfPage pdfPage = new PdfPage();
			pdfPage.pageNumber = this.pageNumber;
			pdfPage.pageName = this.pageName;
			pdfPage.images = this.images;
			pdfPage.decodedBarcodes = new ArrayList<>();
			return pdfPage;
		}
	}

}
