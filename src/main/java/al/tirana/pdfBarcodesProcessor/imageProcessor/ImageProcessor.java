package al.tirana.pdfBarcodesProcessor.imageProcessor;

import java.awt.image.BufferedImage;

public interface ImageProcessor {

	BufferedImage extractBarcodeImage(BufferedImage image);
	
	
	
	BufferedImage rotateImage(BufferedImage image,double angle);

}
