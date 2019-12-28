package al.tirana.pdfBarcodesProcessor;

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
		for (int angle = 0; angle <= 100; angle += 15) {
			BufferedImage rotatedImage = this.imageProcessor.rotateImage(image, angle);
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



}
