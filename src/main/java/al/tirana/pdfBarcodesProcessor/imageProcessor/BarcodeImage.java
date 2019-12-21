package al.tirana.pdfBarcodesProcessor.imageProcessor;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class BarcodeImage {

	private BufferedImage image;
	private boolean skewed;

	public BarcodeImage(BufferedImage image, boolean skewed) {
		super();
		this.image = image;
		this.skewed = skewed;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public boolean isSkewed() {
		return skewed;
	}

	public void setSkewed(boolean skewed) {
		this.skewed = skewed;
	}

}
