package al.tirana.pdfBarcodesProcessor.imageProcessor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import nu.pattern.OpenCV;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class OpenCVImageProcessor implements ImageProcessor {

	public OpenCVImageProcessor() {
		OpenCV.loadLibrary();
	}

	/**
	 * {@inheritDoc} Implemented using the OpenCV library. Not yet ready due to
	 * classifier not being reliable because the training need more data.
	 */
	@Override
	public List<BufferedImage> extractBarcodeImages(BufferedImage image) {
		Mat imageMat = OpenCVHelper.img2Mat(image);
		CascadeClassifier classifier = new CascadeClassifier("src/main/resources/classifier-3/cascade.xml");
		MatOfRect barcodeDetections = new MatOfRect();
		classifier.detectMultiScale(imageMat, barcodeDetections, 1.3, 5, Objdetect.CASCADE_FIND_BIGGEST_OBJECT,
				new Size(200, 130), new Size(800, 400));
		// Drawing boxes
		for (Rect rect : barcodeDetections.toArray()) {
			Core.rectangle(imageMat, // where to draw the box
					new Point(rect.x, rect.y), // bottom left
					new Point(rect.x + rect.width, rect.y + rect.height), // top right
					new Scalar(0, 0, 255), 3);
		}
		List<BufferedImage> imageList = new ArrayList<>();
		imageList.add(OpenCVHelper.mat2Img(imageMat));
		return imageList;
	}

	/**
	 * {@inheritDoc} Implemented using the OpenCV library.
	 */
	@Override
	public BarcodeImage extractBarcodeImage(BufferedImage image) {
		Mat imageMat = OpenCVHelper.img2Mat(image);
		Mat greyMat = new Mat();
		Imgproc.cvtColor(imageMat, greyMat, Imgproc.COLOR_BGR2GRAY);
		Mat imageMatX = new Mat();
		Mat imageMatY = new Mat();
		Imgproc.Sobel(greyMat, imageMatX, CvType.CV_32F, 1, 0, -1, 1, 0);
		Imgproc.Sobel(greyMat, imageMatY, CvType.CV_32F, 0, 1, -1, 1, 0);
		Mat gradientMat = new Mat();
		Core.subtract(imageMatX, imageMatY, gradientMat);
		Core.convertScaleAbs(gradientMat, gradientMat);
		Mat blurredMat = new Mat();
		Imgproc.blur(gradientMat, blurredMat, new Size(9, 9));
		// Mat thresholdMat = new Mat();
		// Imgproc.threshold(blurredMat, thresholdMat, 255, 255, Imgproc.THRESH_BINARY);
		// //Not Need it
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(21, 7));
		Mat closedMat = new Mat();
		Imgproc.morphologyEx(blurredMat, closedMat, Imgproc.MORPH_CLOSE, kernel);
		Imgproc.erode(closedMat, closedMat, kernel);
		Imgproc.dilate(closedMat, closedMat, kernel);
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(closedMat.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL,
				Imgproc.CHAIN_APPROX_SIMPLE);
		Rect cropRect = OpenCVHelper.estimateCropRect(contours, imageMat);

		boolean skewed = false;
		if (!OpenCVHelper.validateCropRect(cropRect, imageMat)) {
			cropRect = OpenCVHelper.fixCropRect(cropRect, imageMat);
			skewed = true;
		}
		Mat cropMat = new Mat(imageMat, cropRect);
		return new BarcodeImage(OpenCVHelper.mat2Img(cropMat), skewed);
	}

	/**
	 * {@inheritDoc} Implemented using the OpenCV library.
	 */
	@Override
	public BufferedImage rotateImage(BufferedImage image, double angle) {
		Mat imageMat = OpenCVHelper.img2Mat(image);
		// Calculate size of new matrix
		double radians = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(radians));
		double cos = Math.abs(Math.cos(radians));
		int newWidth = (int) (imageMat.width() * cos + imageMat.height() * sin);
		int newHeight = (int) (imageMat.width() * sin + imageMat.height() * cos);
		// rotating image
		Point center = new Point(newWidth / 2, newHeight / 2);
		Mat rotMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0); // 1.0 means 100 % scale
		Imgproc.warpAffine(imageMat, imageMat, rotMatrix, imageMat.size());
		return OpenCVHelper.mat2Img(imageMat);
	}

}
