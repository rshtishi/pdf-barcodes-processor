package al.tirana.pdfBarcodesProcessor;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import al.tirana.pdfBarcodesProcessor.barcodeDecoder.BarcodeDecoder;
import al.tirana.pdfBarcodesProcessor.barcodeDecoder.ZxingBarcodeDecoder;
import al.tirana.pdfBarcodesProcessor.imageProcessor.BarcodeImage;
import al.tirana.pdfBarcodesProcessor.imageProcessor.ImageProcessor;
import al.tirana.pdfBarcodesProcessor.imageProcessor.OpenCVImageProcessor;
import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfBoxPdfProcessor;
import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfDocument;
import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfPage;
import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfProcessor;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfBarcodesProcessorImpl implements PdfBarcodesProcessor {

	ImageProcessor imageProcessor;

	public PdfBarcodesProcessorImpl() {
		this.imageProcessor = new OpenCVImageProcessor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PdfDocument processPdfWithMultipleBarcodesPerPage(String filePath) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PdfDocument processPdfWithSingleBarcodePerPage(String filePath) throws Exception {
		PdfProcessor pdfProcessor = new PdfBoxPdfProcessor();
		PdfDocument pdfDocument = pdfProcessor.processPdfFile(filePath);
		pdfDocument.getPdfPageList().forEach(page -> {
			page = processPage(page);
		});
		return pdfDocument;
	}

	/**
	 * @param page
	 * @return Process the images of the pdf page and adds the decoded bar codes.
	 */
	private PdfPage processPage(PdfPage page) {
		page.getImages().forEach(image -> {
			image = convertTo3ByteBGRType(image);
			BarcodeImage barcodeImage = this.imageProcessor.extractBarcodeImage(image);
			String decodedBarcode = decodeImage(barcodeImage.getImage());
			if (decodedBarcode == null) {
				if (barcodeImage.isSkewed()) {
					decodedBarcode = rotateImageUntilDecoded(barcodeImage.getImage(), (i) -> this.decodeImage(i));
				} else {
					BufferedImage rotatedImage = this.imageProcessor.rotateImage(barcodeImage.getImage(), 180);
					decodedBarcode = decodeImage(rotatedImage);
				}
			}
			page.getDecodedBarcodes().add(decodedBarcode);
		});
		return page;
	}

	/**
	 * @param image
	 * @param decodeFunction
	 * @return Rotates the images to find angle in which the bar code can be
	 *         decoded.
	 */
	private String rotateImageUntilDecoded(BufferedImage image, Function<BufferedImage, String> decodeFunction) {
		String decodedBarcode = null;
		for (int angle = 0; angle <= 100; angle += 10) {
			BufferedImage rotatedImage = rotateImageByDegrees(image, angle);
			decodedBarcode = decodeFunction.apply(rotatedImage);
			if ( !StringUtils.isEmpty(StringUtils.trim(decodedBarcode)) ) {
				return decodedBarcode;
			}
		}
		return decodedBarcode;
	}

	/**
	 * @param bufferedImage
	 * @return Extract the information encoded in barcode image
	 */
	private String decodeImage(BufferedImage bufferedImage) {
		BarcodeDecoder barcodeDecoder = new ZxingBarcodeDecoder();
		String decodedBarcode = barcodeDecoder.decode(bufferedImage);
		return decodedBarcode;
	}

	/**
	 * @param image
	 * @return Convert the BufferedImage to type that we can work with opencv
	 *         libarary.
	 */
	private BufferedImage convertTo3ByteBGRType(BufferedImage image) {
		BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_3BYTE_BGR);
		convertedImage.getGraphics().drawImage(image, 0, 0, null);
		return convertedImage;
	}

	/**
	 * 
	 * @param img
	 * @param angle
	 * @return Rotate with opencv did not work well Temporary implmentation of
	 *         rotate
	 */
	public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads));
		double cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);
		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_INDEXED);
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);
		int x = w / 2;
		int y = h / 2;
		at.rotate(rads, x, y);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotated = scaleOp.filter(img, rotated);
		return rotated;
	}

}
