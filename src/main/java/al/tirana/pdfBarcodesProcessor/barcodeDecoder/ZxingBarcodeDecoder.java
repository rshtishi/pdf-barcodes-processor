package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ZxingDecoder implements BarcodeDecoder {

	@Override
	public String decode(BufferedImage image,ZxingDecoderType type){
		Reader reader =  ZxingFactory.getInstance().getBarcodeReader(type);
		Map<DecodeHintType, Object> decodeHintTypes = new HashMap<DecodeHintType, Object>();
		decodeHintTypes.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			Result result = reader.decode(bitmap,decodeHintTypes);
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
