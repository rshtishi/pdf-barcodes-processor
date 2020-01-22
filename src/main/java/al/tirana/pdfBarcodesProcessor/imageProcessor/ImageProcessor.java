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
	 * Set the ratio between between image size and bar code size inside image.
	 * width = height = bar code size / image size
	 * 
	 * @param ratio
	 */
	void setBarcodeRatioToImage(double ratio);

	/**
	 * Set the ratio between image size and bar code size inside image.
	 * widthRatio = bar code width  /  image width 
	 * heightRation = bar code height / image height 
	 * 
	 * @param widthRatio
	 * @param heightRatio
	 */
	void setBarcodeRatioToImage(double widthRatio, double heightRatio);

	/**
	 * It crop all area where bar code images are located and returns the list of
	 * bar code images. To be used when image might have more that one bar code.
	 * 
	 * @param image
	 * @return
	 */
	List<BufferedImage> extractBarcodeImages(BufferedImage image);

	/**
	 * It crop the area  where the bar code image is located. It return the bar code image
	 * along with the information if the bar code image is skewed. If it is not skewed
	 * no rotation it is need or 180 degree rotation it is needed. If it is skewed ,
	 * it is need to try rotating with different angles.
	 * 
	 * @param image
	 * @return
	 */
	BarcodeImage extractBarcodeImage(BufferedImage image);

	/**
	 * Rotates the image to a specific angle.
	 * 
	 * @param image
	 * @param angle
	 * @return
	 */
	BufferedImage rotateImage(BufferedImage image, double angle);

}
