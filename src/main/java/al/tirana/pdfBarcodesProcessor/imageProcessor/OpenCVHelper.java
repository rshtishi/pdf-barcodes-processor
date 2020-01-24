package al.tirana.pdfBarcodesProcessor.imageProcessor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class OpenCVHelper {

	/**
	 * The Mat type image is converted to BufferedImage type.
	 * 
	 * @param mat
	 * @return
	 */
	public static BufferedImage mat2Img(Mat mat) {
		BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		mat.get(0, 0, data);
		return image;
	}

	/**
	 * The BufferedImage type image is converted to Mat type.
	 * 
	 * @param image
	 * @return
	 */
	public static Mat img2Mat(BufferedImage image) {
		image = convertTo3ByteBGRType(image);
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		return mat;
	}

	/**
	 * Convert the BufferedImage to type that we can work with opencv libarary.
	 * 
	 * @param image
	 * @return
	 */
	private static BufferedImage convertTo3ByteBGRType(BufferedImage image) {
		BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_3BYTE_BGR);
		convertedImage.getGraphics().drawImage(image, 0, 0, null);
		return convertedImage;
	}

	/**
	 * Estimates the crop area and return a rectangle area. This rectangle area is
	 * area that will be cropped from the image.
	 * 
	 * @param contours
	 * @param imageMat
	 * @return
	 */
	public static Rect estimateCropRect(List<MatOfPoint> contours, Mat imageMat) {
		double maxArea = 0;
		MatOfPoint max_contour = new MatOfPoint();
		Iterator<MatOfPoint> iterator = contours.iterator();
		while (iterator.hasNext()) {
			MatOfPoint contour = iterator.next();
			double area = Imgproc.contourArea(contour);
			if (area > maxArea) {
				maxArea = area;
				max_contour = contour;
			}
		}
		RotatedRect rect = Imgproc.minAreaRect(new MatOfPoint2f(max_contour.toArray()));
		Rect cropRect = rect.boundingRect();
		return cropRect;
	}

	/**
	 * Validates the crop area goes outside of boundary of image.
	 * 
	 * @param cropRect
	 * @param imageMat
	 * @return
	 */
	public static boolean validateCropRect(Rect cropRect, Mat imageMat) {
		boolean validation = true;
		if (cropRect.x < 0)
			validation = false;
		if (cropRect.y < 0)
			validation = false;
		if (cropRect.width > imageMat.cols() - cropRect.x)
			validation = false;
		if (cropRect.height > imageMat.rows() - cropRect.y)
			validation = false;
		return validation;
	}

	/**
	 * Fixes the crop area if it goes outside the boundary of image.
	 * 
	 * @param cropRect
	 * @param imageMat
	 * @return
	 */
	public static Rect fixCropRect(Rect cropRect, Mat imageMat) {
		if (cropRect.x < 0)
			cropRect.x = 0;
		if (cropRect.y < 0)
			cropRect.y = 0;
		if (cropRect.width > imageMat.cols() - cropRect.x)
			cropRect.width = imageMat.width() - cropRect.x;
		if (cropRect.height > imageMat.rows() - cropRect.y)
			cropRect.height = imageMat.height() - cropRect.y;
		return cropRect;
	}

}
