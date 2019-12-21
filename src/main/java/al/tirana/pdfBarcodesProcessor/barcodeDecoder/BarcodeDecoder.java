package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Rando Shtishi
 *
 */
public interface BarcodeDecoder {

	/**
	 * @param image
	 * @param type
	 * @return
	 * @throws Exception Decodes a bar code images which type is known.
	 */
	public String decode(BufferedImage image, ZxingDecoderType type);

	/**
	 * @param image
	 * @return
	 * @throws Exception Decodes bar code images which type is unknown.
	 */
	public String decode(BufferedImage image);

}
