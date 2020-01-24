package al.tirana.pdfBarcodesProcessor;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import al.tirana.pdfBarcodesProcessor.barcodeDecoder.BarcodeDecoder;
import al.tirana.pdfBarcodesProcessor.barcodeDecoder.ZxingBarcodeDecoder;
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

	private PdfProcessor pdfProcessor;
	private ImageProcessor imageProcessor;
	private BarcodeDecoder barcodeDecoder;

	private PdfBarcodesProcessorImpl() {
	}
	
	public static class Builder {
		
		private ImageProcessor imageProcessor;
		private BarcodeDecoder barcodeDecoder;
		private PdfProcessor pdfProcessor;
		
		public Builder() {
			this.pdfProcessor = new PdfBoxPdfProcessor();
			this.imageProcessor = new OpenCVImageProcessor();
			this.barcodeDecoder = new ZxingBarcodeDecoder();
		}

		public Builder pdfProcessor(PdfProcessor pdfProcessor) {
			this.pdfProcessor = pdfProcessor;
			return this;
		}
		
		public Builder imageProcessor(ImageProcessor imageProcessor) {
			this.imageProcessor = new OpenCVImageProcessor();
			return this;
		}
		
		public Builder barcodeDecoder(BarcodeDecoder barcodeDecoder) {
			this.barcodeDecoder = barcodeDecoder;
			return this;
		}
		
		public PdfBarcodesProcessorImpl build() {
			PdfBarcodesProcessorImpl pdfProcessor = new PdfBarcodesProcessorImpl();
			pdfProcessor.pdfProcessor = this.pdfProcessor;
			pdfProcessor.imageProcessor = this.imageProcessor;
			pdfProcessor.barcodeDecoder = this.barcodeDecoder;
			return pdfProcessor;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PdfDocument processPdfBarcodesPerPage(String filePath) throws Exception {
		PdfDocument pdfDocument = this.pdfProcessor.processPdfFile(filePath);
		pdfDocument.getPdfPageList().forEach(page -> {
			page = processPage(page);
		});
		return pdfDocument;
	}

	/**
	 * Process the images of the pdf page and adds the decoded bar codes.
	 * 
	 * @param page
	 * @return
	 */
	private PdfPage processPage(PdfPage page) {
		page.getImages().forEach(image -> {
			List<BufferedImage> extractedImages = this.imageProcessor.extractBarcodeImages(image);
			processExtractedImages(extractedImages, page);
		});
		return page;
	}
	
	/**
	 * 
	 * @param extractedImages
	 * @param page
	 */
	private void processExtractedImages(List<BufferedImage> extractedImages, PdfPage page) {
		extractedImages.forEach(image -> {
			BufferedImage ximage = this.imageProcessor.extractBarcodeImage(image);
			String decodedBarcode = this.barcodeDecoder.decode(ximage);
			if (decodedBarcode == null) {
				decodedBarcode = rotateImageUntilDecoded(image, (i) -> this.barcodeDecoder.decode(i));
			}
			if (decodedBarcode != null) {
				page.getDecodedBarcodeImageMap().put(decodedBarcode, ximage);
			}
		});

	}

	/**
	 * Rotates the images to find angle in which the bar code can be decoded.
	 * 
	 * @param image
	 * @param decodeFunction
	 * @return
	 */
	private String rotateImageUntilDecoded(BufferedImage image, Function<BufferedImage, String> decodeFunction) {
		String decodedBarcode = null;
		for (int angle = 0; angle <= 100; angle += 15) {
			BufferedImage rotatedImage = this.imageProcessor.rotateImage(image, angle);
			decodedBarcode = decodeFunction.apply(rotatedImage);
			if (!StringUtils.isEmpty(StringUtils.trim(decodedBarcode))) {
				return decodedBarcode;
			}
		}
		return decodedBarcode;
	}



}
