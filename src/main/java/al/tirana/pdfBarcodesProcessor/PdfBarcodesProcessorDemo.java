package al.tirana.pdfBarcodesProcessor;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfBarcodesProcessorDemo {
	
	private int totalPdfPages = 0;

	public void processPdfFile(String fileStr) throws Exception {

		Hashtable<DecodeHintType, Object> decodeHintTypes = new Hashtable<DecodeHintType, Object>();
		decodeHintTypes.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		Reader reader = new MultiFormatReader();
		File file = new File(fileStr);
		if (file.exists()) {
			PDDocument document = PDDocument.load(file);
			PDPageTree pages = document.getDocumentCatalog().getPages();
			for (PDPage page : pages) {
				this.totalPdfPages++;
				PDResources resources = page.getResources();
				Iterable<COSName> objectNames = resources.getXObjectNames();
				int imageNum = 0;
				for (COSName objectName : objectNames) {
					PDXObject object = resources.getXObject(objectName);
					if (object instanceof PDImageXObject) {
						PDImageXObject imageObject = (PDImageXObject) object;
						imageNum++;
						System.out.println("Image Number: " + imageNum);
						BufferedImage image = imageObject.getImage();
						int degree = 20;
						for (int i = 0; i < 360 / degree; i++) {
							 BufferedImage rotatedImage = rotateImageByDegrees(image, i*degree);
							 Result result = extractBarcode(rotatedImage, reader, decodeHintTypes);
							 if(result!=null) {
								 System.out.println(result.getText());
								 break;
							 }
						}
					}
				}
			}
			System.out.println("Total pages: "+this.totalPdfPages);
		} else {
			throw new FileNotFoundException("File not found ");
		}
	}

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
	
	public Result extractBarcode(BufferedImage bufferedImage,Reader reader, Map<DecodeHintType, ?> decodeHintTypes) {
		Result result = null;
		try {
			LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			result = reader.decode(bitmap, decodeHintTypes);
		} catch (Exception e) {
		}
		return result;	
	}

	
	

}
