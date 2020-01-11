package al.tirana.pdfBarcodesProcessor.imageProcessor;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import nu.pattern.OpenCV;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class OpenCVTrainingClassifierTest {

	private String imagesPath = "/home/rando/Pictures/opencv/images";
	private String posImagesPath = "/home/rando/Pictures/opencv/pos";
	private String negImagesPath = "/home/rando/Pictures/opencv/neg";
	private String parentDirPath = "/home/rando/Pictures/opencv/";
	private ImageProcessor imageProcessor;

	private int[][] coord = { { 0, 0, 70, 10 }, { 15, 10, 70, 10 }, { 5, 15, 70, 10 }, { 10, 25, 70, 10 } };
	private String[] bgImages = { "src/test/resources/opencv/document1.jpg", "src/test/resources/opencv/document2.jpg",
			"src/test/resources/opencv/document3.jpg", "src/test/resources/opencv/document4.png",
			"src/test/resources/opencv/document5.png", "src/test/resources/opencv/document6.png",
			"src/test/resources/opencv/document7.png", "src/test/resources/opencv/document8.jpg" };

	private BarcodeFormat[] barcodes = { BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat.CODABAR,
			BarcodeFormat.CODE_128, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13, BarcodeFormat.ITF, BarcodeFormat.UPC_A,
			BarcodeFormat.UPC_E };

	/**
	 * 
	 * @throws Exception Generating the information need to run the classifier.
	 */
	@Before
	public void setup() throws Exception {
		OpenCV.loadLibrary();
		imageProcessor = new OpenCVImageProcessor();
		File posImagesDir = generatePosDir();
		generatePosInfoFile(posImagesDir);
		generateNegImages();
		generateNegInfoFile();

	}

	/**
	 * To train the classifier you need to install open cv in your operating system
	 * generate negative images,positive images ,pos.info,neg.info, pos.vec train
	 * the classifier with opencv
	 */
	@Test
	public void trainClassifier() {
		//TO DO
		//Generate Vec File
		//Run the train process
	}


	/**
	 * @return
	 * @throws Exception Generate the directory containing images with bar codes
	 *                   that are going to be used to train the classifier.
	 */
	private File generatePosDir() throws Exception {
		File posImagesDir = new File(posImagesPath);

		if (posImagesDir.exists() && posImagesDir.list().length == 0) {

			// creating barcode without background
			for (BarcodeFormat type : barcodes) {
				String data = null;
				if (type == BarcodeFormat.EAN_8 || type == BarcodeFormat.UPC_E) {
					data = "12345670";
					generateBarcode(data, type);
				} else if (type == BarcodeFormat.EAN_13 || type == BarcodeFormat.UPC_A) {
					data = "123456789012";
					generateBarcode(data, type);
				} else if (type == BarcodeFormat.ITF) {
					for (int i = 10; i < 50; i++) {
						data = Integer.toString(i);
						generateBarcode(data, type);
					}
				} else {
					for (int i = 0; i < 50; i++) {
						data = Integer.toString(i);
						generateBarcode(data, type);

					}
				}

			}

			// create barcode with background images
			for (BarcodeFormat type : barcodes) {
				if (type != BarcodeFormat.EAN_8 && type != BarcodeFormat.UPC_E && type != BarcodeFormat.EAN_13
						&& type != BarcodeFormat.UPC_A && type != BarcodeFormat.ITF) {
					for (int data = 0; data <= 70; data++) {
						for (int coordx = 0; coordx < coord.length; coordx++) {
							for (int cimg = 0; cimg < bgImages.length; cimg++) {
								generateBarcodeWithBackground(Integer.toString(data), type, coordx, bgImages[cimg]);
							}
						}
					}
				}
			}
		}
		return posImagesDir;
	}

	/**
	 * generate barcode image with background from behind
	 * 
	 * @param data
	 * @param type
	 * @param rIndex
	 * @param bgImgPath
	 * @throws Exception
	 */
	private void generateBarcodeWithBackground(String data, BarcodeFormat type, int rIndex, String bgImgPath)
			throws Exception {
		BufferedImage barcodeImage = generateBarcodeImage(data, type, coord[rIndex][2], coord[rIndex][3]);
		Mat barcodeImageMat = OpenCVHelper.img2Mat(barcodeImage);
		Mat bgImageMat = Highgui.imread(bgImgPath);
		Rect roi = new Rect(coord[rIndex][0], coord[rIndex][1], barcodeImageMat.cols(), barcodeImageMat.rows());
		System.out.println(barcodeImageMat.cols() + ":" + barcodeImageMat.rows());
		Mat bgImageSubMat = bgImageMat.submat(roi);
		barcodeImageMat.copyTo(bgImageSubMat);
		String bgName = bgImgPath.split("\\/")[bgImgPath.split("\\/").length - 1];
		String savePath = "/home/rando/Pictures/opencv/pos/coord" + rIndex + "-" + type + "-" + data + "-" + bgName;
		System.out.println(savePath);
		Highgui.imwrite(savePath, bgImageMat);
	}

	/**
	 * 
	 * @param data
	 * @param type
	 * @throws Exception
	 */
	private void generateBarcode(String data, BarcodeFormat type) throws Exception {
		BufferedImage image = generateBarcodeImage(data, type, 100, 40);
		String output = posImagesPath.concat("/").concat(type.name()).concat("-").concat(data).concat(".png");
		ImageIO.write(image, "png", new File(output));

	}

	/**
	 * 
	 * @param data
	 * @param type
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	private BufferedImage generateBarcodeImage(String data, BarcodeFormat type, int width, int height)
			throws Exception {
		MultiFormatWriter barcodeWriter = new MultiFormatWriter();
		BitMatrix barcodeBitMatrix = barcodeWriter.encode(data, type, width, height);
		return MatrixToImageWriter.toBufferedImage(barcodeBitMatrix);
	}

	/**
	 * Generates the file that contains the information for each image that is in
	 * positive directory(image path,number of bar codes in image, coordinate x,
	 * coordinate y, width, height).
	 * 
	 * @param posImagesDir
	 * @throws Exception
	 */
	private void generatePosInfoFile(File posImagesDir) throws Exception {
		File barcodeInfoFile = new File(parentDirPath.concat("barcodes.info"));
		if (!barcodeInfoFile.exists()) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(barcodeInfoFile));
			for (File file : posImagesDir.listFiles()) {
				BufferedImage image = ImageIO.read(file);
				String line = file.getAbsolutePath();

				if (file.getName().contains("coord0")) {
					line = line.concat(" 1 ").concat(Integer.toString(coord[0][0])).concat(" ")
							.concat(Integer.toString(coord[0][1])).concat(" ").concat(Integer.toString(coord[0][2]))
							.concat(" ").concat(Integer.toString(coord[0][3]));
				} else if (file.getName().contains("coord1")) {
					line = line.concat(" 1 ").concat(Integer.toString(coord[1][0])).concat(" ")
							.concat(Integer.toString(coord[1][1])).concat(" ").concat(Integer.toString(coord[1][2]))
							.concat(" ").concat(Integer.toString(coord[1][3]));
				} else if (file.getName().contains("coord2")) {
					line = line.concat(" 1 ").concat(Integer.toString(coord[2][0])).concat(" ")
							.concat(Integer.toString(coord[2][1])).concat(" ").concat(Integer.toString(coord[2][2]))
							.concat(" ").concat(Integer.toString(coord[2][3]));
				} else if (file.getName().contains("coord3")) {
					line = line.concat(" 1 ").concat(Integer.toString(coord[3][0])).concat(" ")
							.concat(Integer.toString(coord[3][1])).concat(" ").concat(Integer.toString(coord[3][2]))
							.concat(" ").concat(Integer.toString(coord[3][3]));
				} else {
					line = line.concat(" 1 0 0 ").concat(Integer.toString(image.getWidth())).concat(" ")
							.concat(Integer.toString(image.getHeight()));
				}
				writer.write(line);
				writer.write("\n");
			}
			writer.close();
		}
	}

	/**
	 * Adjust the images size to be acceptable for opencv.
	 * 
	 * @throws IOException
	 */
	private void generateNegImages() throws IOException {
		imageProcessor = new OpenCVImageProcessor();
		File imagesDir = new File(this.imagesPath);
		if (imagesDir.exists()) {
			for (File imageFile : imagesDir.listFiles()) {
//				BufferedImage image = ImageIO.read(imageFile);
//				if (image.getWidth() < image.getHeight()) {
//					image = this.imageProcessor.rotateImage(image, 90);
//				}
				// resize
				Mat imageMat = Highgui.imread(imageFile.getAbsolutePath());
				Size size = new Size(100, 40);
				Imgproc.resize(imageMat, imageMat, size);
				//image = OpenCVHelper.mat2Img(imageMat);
				// end resize
				String newFilePath = negImagesPath + "/" + imageFile.getName();
				String[] nameArr = imageFile.getName().split("\\.");
				int last = nameArr.length - 1;
				String fileType = nameArr[last];
				File newFile = new File(newFilePath);
				if (!newFile.exists()) {
					//ImageIO.write(image, fileType, newFile);
					Highgui.imwrite(newFile.getAbsolutePath(), imageMat);
				}
			}
		}

	}

	/**
	 * Generates the file that contains the information for each image that is in
	 * negative directory(only image path).
	 * 
	 * @throws Exception
	 */
	private void generateNegInfoFile() throws Exception {
		File negImagesDir = new File(negImagesPath);
		File negInfoFile = new File(parentDirPath.concat("bg.txt"));
		if (!negInfoFile.exists()) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(negInfoFile));
			for (File file : negImagesDir.listFiles()) {
				writer.write(file.getAbsolutePath());
				writer.write("\n");
			}
			writer.close();
		}
	}

}
