package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import java.awt.image.BufferedImage;

public interface BarcodeDecoder {
	
	public String decode(BufferedImage image, ZxingDecoderType type) throws Exception;
	public String decode(BufferedImage image) throws Exception;

}
