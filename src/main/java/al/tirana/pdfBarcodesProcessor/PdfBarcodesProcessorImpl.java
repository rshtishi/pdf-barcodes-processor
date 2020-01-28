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
	 * Process the images of the pdf page and extract the bar codes images from the
	 * pdf page.
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
	 * Process the extracted bar code images and decodes the bar code images. And if
	 * the bar code is decoded, add the bar code image and decoded text in pdf page.
	 * 
	 * @param extractedImages
	 * @param page
	 */
	private void processExtractedImages(List<BufferedImage> extractedImages, PdfPage page) {
		extractedImages.forEach(image -> {
			String decodedBarcode = this.decodeBarcodeImage(image);
			if (decodedBarcode != null) {
				page.getDecodedBarcodeImageMap().put(decodedBarcode, image);
			} else if (decodedBarcode == null) {
				// extract again with open cv features
				BufferedImage ximage = this.imageProcessor.extractBarcodeImage(image);
				decodedBarcode = this.decodeBarcodeImage(image);
				if (decodedBarcode != null) {
					page.getDecodedBarcodeImageMap().put(decodedBarcode, image);
				}
			}
		});
	}

	/**
	 * Decoding bar code image.
	 * 
	 * @param image
	 * @return
	 */
	private String decodeBarcodeImage(BufferedImage image) {
		String decodedBarcode = this.barcodeDecoder.decode(image);
		if (decodedBarcode != null) {
			return decodedBarcode;
		}
		decodedBarcode = rotateImageUntilDecoded(image, (i) -> this.barcodeDecoder.decode(i));
		return decodedBarcode;
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
