package al.tirana.pdfBarcodesProcessor.pdfprocessor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * 
 * @author Rando Shtishi
 *
 */
public class PdfBoxPdfProcessor implements PdfProcessor {

	/**
	 * {@inheritDoc} Implemented with PdfBox library.
	 */
	@Override
	public PdfDocument processPdfFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists()) {
			PDDocument document = PDDocument.load(file);
			PDPageTree pages = document.getDocumentCatalog().getPages();
			int pageNumber = 0;
			List<PdfPage> pdfPageList = new ArrayList<>();
			for (PDPage page : pages) {
				PdfPage pdfPage = new PdfPage.Builder().pageNumber(pageNumber)
						.pageName(generatePdfPageName(file, pageNumber)).images(extractImages(page)).build();
				pdfPageList.add(pdfPage);
				pageNumber++;
			}
			document.close();
			return new PdfDocument.Builder().totalPages(pageNumber).pdfPages(pdfPageList).build();
		} else {
			throw new FileNotFoundException("File not found ");
		}
	}

	/**
	 * 
	 * @param file
	 * @param pageNumber
	 * @return Generated the pdf page name from the pdf filename and page no.
	 */
	private String generatePdfPageName(File file, int pageNumber) {
		String[] fileNameArr = file.getName().split("\\.");
		return fileNameArr[0].concat("#page_").concat(Integer.toString(pageNumber)).concat(".").concat(fileNameArr[1]);
	}

	/**
	 * 
	 * @param page
	 * @return
	 * @throws Exception Extract all images from a pdf page.
	 */
	private List<BufferedImage> extractImages(PDPage page) throws Exception {
		List<BufferedImage> imageList = new ArrayList<>();
		PDResources resources = page.getResources();
		Iterable<COSName> objectNames = resources.getXObjectNames();
		for (COSName objectName : objectNames) {
			PDXObject object = resources.getXObject(objectName);
			if (object instanceof PDImageXObject) {
				PDImageXObject imageObject = (PDImageXObject) object;
				BufferedImage image = imageObject.getImage();
				imageList.add(image);
			}
		}
		return imageList;
	}

}
