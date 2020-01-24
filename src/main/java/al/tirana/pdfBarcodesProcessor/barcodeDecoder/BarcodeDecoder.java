package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Rando Shtishi
 *
 */
public interface BarcodeDecoder {

	/**
	 * Decodes a bar code images which type is known.
	 * 
	 * @param image
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String decode(BufferedImage image, ZxingDecoderType type);

	/**
	 * Decodes bar code images which type is unknown.
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	public String decode(BufferedImage image);

}
