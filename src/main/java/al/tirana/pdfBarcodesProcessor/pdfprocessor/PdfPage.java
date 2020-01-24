package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfPage {

	private int pageNumber;
	private List<BufferedImage> images;
	private String pageName;
	private Map<String, BufferedImage> decodedBarcodeImageMap;

	public Map<String, BufferedImage> getDecodedBarcodeImageMap() {
		return decodedBarcodeImageMap;
	}

	private PdfPage() {
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

	/**
	 * Returns the list with all images that are decoded.
	 * 
	 * @return
	 */
	public List<BufferedImage> getAllDecodedBarcodeImages() {
		return new ArrayList<>(decodedBarcodeImageMap.values());
	}

	/**
	 * Returns the list with all text from decoded bar codes
	 * 
	 * @return
	 */
	public List<String> getAllDecodedBarcodesList() {
		return new ArrayList<>(decodedBarcodeImageMap.keySet());
	}

	/**
	 * Saves all decoded bar codes images inside a directory specified in parameter,
	 * where image name is bar code decoded text.
	 * 
	 * @param dirPath
	 */
	public void saveDecodedBarcodeImages(String dirPath) {
		decodedBarcodeImageMap.keySet().forEach(name -> {
			String filePath = dirPath + "/" + name + ".jpg";
			BufferedImage image = decodedBarcodeImageMap.get(name);
			try {
				ImageIO.write(image, "png", new File(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}

		});
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
			pdfPage.decodedBarcodeImageMap = new HashMap<>();
			return pdfPage;
		}
	}

}
