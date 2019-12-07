package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code39Reader;

public class ZxingDecoder implements BarcodeDecoder {

	@Override
	public String decode(BufferedImage image,ZxingDecoderType type){
		Reader reader =  ZxingFactory.getInstance().getBarcodeReader(type);
		Hashtable<DecodeHintType, Object> decodeHintTypes = new Hashtable<DecodeHintType, Object>();
		decodeHintTypes.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			Result result = reader.decode(bitmap);
			return result.getText();
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public String decode(BufferedImage image) throws Exception {
		return decode(image, ZxingDecoderType.DEFAULT);
	}

}
