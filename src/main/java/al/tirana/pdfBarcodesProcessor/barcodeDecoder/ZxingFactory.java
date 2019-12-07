package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.oned.CodaBarReader;
import com.google.zxing.oned.Code128Reader;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.Code93Reader;
import com.google.zxing.oned.EAN13Reader;
import com.google.zxing.oned.EAN8Reader;
import com.google.zxing.oned.ITFReader;
import com.google.zxing.oned.UPCAReader;
import com.google.zxing.oned.UPCEReader;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.expanded.RSSExpandedReader;

public class ZxingFactory {
	
	public static ZxingFactory instance = null;
	
	private ZxingFactory() {
	}
	
	public static ZxingFactory getInstance() {
		if(instance==null) {
			instance = new ZxingFactory();
		}
		return instance;
	}

	public Reader getBarcodeReader(ZxingDecoderType type) {
		Reader reader = null;
		switch (type) {
		case CODE_39:
			reader = new Code39Reader();
			break;
		case CODE_93:
			reader = new Code93Reader();
			break;
		case CODE_128:
			reader = new Code128Reader();
			break;
		case CODABAR:
			reader = new CodaBarReader();
			break;
		case RSS_14:
			reader= new RSS14Reader();
		case RSS_EXPANDED:
			reader = new RSSExpandedReader();
			break;
		case ITF:
			reader = new ITFReader();
			break;
		case UPCA:
			reader = new UPCAReader();
			break;
		case UPCE:
			reader = new UPCEReader();
			break;
		case EAN_13:
			reader = new EAN13Reader();
			break;
		case EAN_8:
			reader = new EAN8Reader();
			break;
		default:
			reader = new MultiFormatReader();
			break;
		}
		return reader;
	}

}
