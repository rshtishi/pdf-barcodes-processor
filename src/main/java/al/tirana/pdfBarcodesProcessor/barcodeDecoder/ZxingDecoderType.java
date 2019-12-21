package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import com.google.zxing.BarcodeFormat;

/**
 * 
 * @author Rando Shtishi
 *
 */
public enum ZxingDecoderType {

	CODE_39(BarcodeFormat.CODE_39), CODE_93(BarcodeFormat.CODE_93), CODE_128(BarcodeFormat.CODE_128),
	CODABAR(BarcodeFormat.CODABAR), RSS_14(BarcodeFormat.RSS_14), RSS_EXPANDED(BarcodeFormat.RSS_EXPANDED),
	ITF(BarcodeFormat.ITF), UPCA(BarcodeFormat.UPC_A), UPCE(BarcodeFormat.UPC_E), EAN_13(BarcodeFormat.EAN_13),
	EAN_8(BarcodeFormat.EAN_8), DEFAULT(null);

	private BarcodeFormat barcodeFormat;

	ZxingDecoderType(BarcodeFormat barcodeFormat) {
		this.barcodeFormat = barcodeFormat;
	}

	public BarcodeFormat getBarcodeFormat() {
		return barcodeFormat;
	}

}
