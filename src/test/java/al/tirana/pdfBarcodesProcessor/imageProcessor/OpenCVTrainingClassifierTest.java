package al.tirana.pdfBarcodesProcessor.imageProcessor;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import org.junit.Before;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class OpenCVTrainingClassifierTest {

	private String posImagesPath = "/home/rando/Pictures/opencv/pos";
	private String negImagesPath = "/home/rando/Pictures/opencv/neg";
	private String parentDirPath = "/home/rando/Pictures/opencv/";

	private BarcodeFormat[] barcodes = { BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat.CODABAR,
			BarcodeFormat.CODE_128, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13, BarcodeFormat.ITF, BarcodeFormat.UPC_A,
			BarcodeFormat.UPC_E };

	/**
	 * 
	 * @throws Exception Generating the information need to run the classifier.
	 */
	@Before
	public void setup() throws Exception {
		File posImagesDir = generatePosDir();
		generatePosInfoFile(posImagesDir);
		generateNegInfoFile();

	}

	/**
	 * To train the classifier you need to install open cv in your operating system
	 * generate negative images,positive images ,pos.info,neg.info, pos.vec train
	 * the classifier with opencv
	 */
	public void trainClassifier() {
	}

	/**
	 * @return
	 * @throws Exception Generate the directory containing images with bar codes
	 *                   that are going to be used to train the classifier.
	 */
	private File generatePosDir() throws Exception {
		File posImagesDir = new File(posImagesPath);
		if (posImagesDir.exists() && posImagesDir.list().length == 0) {
			for (BarcodeFormat type : barcodes) {
				String data = null;
				if (type == BarcodeFormat.EAN_8 || type == BarcodeFormat.UPC_E) {
					data = "12345670";
					generateBarcode(data, type);
				} else if (type == BarcodeFormat.EAN_13 || type == BarcodeFormat.UPC_A) {
					data = "123456789012";
					generateBarcode(data, type);
				} else if (type == BarcodeFormat.ITF) {
					for (int i = 10; i < 100; i++) {
						data = Integer.toString(i);
						generateBarcode(data, type);
					}
				} else {
					for (int i = 0; i < 100; i++) {
						data = Integer.toString(i);
						generateBarcode(data, type);
					}
				}

			}
		}
		return posImagesDir;
	}

	/**
	 * 
	 * @param data
	 * @param type
	 * @throws Exception Generates a image that contains a bar code.
	 */
	private void generateBarcode(String data, BarcodeFormat type) throws Exception {
		MultiFormatWriter barcodeWriter = new MultiFormatWriter();
		BitMatrix barcodeBitMatrix = barcodeWriter.encode(data, type, 100, 40);
		BufferedImage image = MatrixToImageWriter.toBufferedImage(barcodeBitMatrix);
		String output = posImagesPath.concat("/").concat(type.name()).concat("-").concat(data).concat(".png");
		ImageIO.write(image, "png", new File(output));

	}

	/**
	 * @param posImagesDir
	 * @throws Exception Generates the file that contains the information for each
	 *                   image that is in positive directory(image path,number of
	 *                   bar codes in image, coordinate x, coordinate y, width,
	 *                   height).
	 */
	private void generatePosInfoFile(File posImagesDir) throws Exception {
		File barcodeInfoFile = new File(parentDirPath.concat("barcodes.info"));
		if (!barcodeInfoFile.exists()) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(barcodeInfoFile));
			for (File file : posImagesDir.listFiles()) {
				BufferedImage image = ImageIO.read(file);
				String line = file.getAbsolutePath().concat(" 1 0 0 ").concat(Integer.toString(image.getWidth()))
						.concat(" ").concat(Integer.toString(image.getHeight()));

				writer.write(line);
				writer.write("\n");
			}
			writer.close();
		}
	}

	/**
	 * @throws Exception Generates the file that contains the information for each
	 *                   image that is in negative directory(only image path).
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
