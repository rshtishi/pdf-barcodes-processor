package al.tirana.pdfBarcodesProcessor.barcodeDecoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class ZxingDecoderTest {
	
	private ZxingDecoder decoder;
	
	@Before
	public void setup() {
		decoder = new ZxingDecoder();
	}

	@Test
	public void testDecode() throws Exception {
		//setup
		String file = "src/test/resources/img-test3.jpg";
		BufferedImage image = ImageIO.read(new File(file));
		//execute
		String result1 = decoder.decode(image,ZxingDecoderType.CODE_39);
		String result2 = decoder.decode(image);
		String result3 = decoder.decode(image,ZxingDecoderType.CODABAR);
		//verify
		String expectedResult = "12345F";
		assertEquals(result1, expectedResult);
		assertEquals(result2, expectedResult);
		assertNotEquals(result3, expectedResult);
	}

}
