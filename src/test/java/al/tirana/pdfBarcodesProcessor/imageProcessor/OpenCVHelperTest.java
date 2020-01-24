package al.tirana.pdfBarcodesProcessor.imageProcessor;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfBoxPdfProcessor;
import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfDocument;
import al.tirana.pdfBarcodesProcessor.pdfprocessor.PdfProcessor;
import nu.pattern.OpenCV;

public class OpenCVHelperTest {

	@Before
	public void setup() throws Exception {
		OpenCV.loadLibrary();
	}

	@Test
	public void img2MatTest() throws Exception {
		// setup
		String filePath = "src/test/resources/pdf-test1.pdf";
		PdfProcessor pdfProcessor = new PdfBoxPdfProcessor();
		PdfDocument pdfDocument = pdfProcessor.processPdfFile(filePath);
		BufferedImage tstImage = pdfDocument.getPdfPageList().get(0).getImages().get(0);
		// execute
		Mat imageMat = OpenCVHelper.img2Mat(tstImage);
		// verify
		assertEquals(tstImage.getWidth(), imageMat.width());
		assertEquals(tstImage.getHeight(), imageMat.height());
	}

	@Test
	public void mat2ImgTest() {
		// setup
		String filePath = "src/test/resources/img-test1.png";
		Mat tstImageMat = Highgui.imread(filePath);
		// execute
		BufferedImage image = OpenCVHelper.mat2Img(tstImageMat);
		// verify
		assertEquals(tstImageMat.width(), image.getWidth());
		assertEquals(tstImageMat.height(), image.getHeight());
	}

}
