package al.tirana.pdfBarcodesProcessor.imageProcessor;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 
 * @author Rando Shtishi
 *
 */
public interface ImageProcessor {

	/**
	 * @param image
	 * @return It crop all area where bar code images are located and returns the
	 *         list of bar code images. To be used when image might have more that
	 *         one image.
	 */
	List<BufferedImage> extractBarcodeImages(BufferedImage image);

	/**
	 * 
	 * @param image
	 * @return It crop are where the bar code image is located. It return the bar
	 *         code image along with information if the bar code image is skewed. If
	 *         it is not skewed no rotation it is need or 180 degree rotation it is
	 *         needed. If it is skewed , it is need to try rotating with different
	 *         angles.
	 */
	BarcodeImage extractBarcodeImage(BufferedImage image);

	/**
	 * 
	 * @param image
	 * @param angle
	 * @return Rotates the image to a specific angle.
	 */
	BufferedImage rotateImage(BufferedImage image, double angle);

}
