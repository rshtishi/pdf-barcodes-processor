# pdf-barcodes-processor
A library that offers the functionalities for processing bar codes.

pdf-barcodes-processor inlcudes the following dependencies:

  * org.apache.pdfbox , version: 2.0.16 ->  used for processing the pdf, extracting pdf page, and extracting images from pdf page
  * com.google.zxing ,  version: 3.3.3  -> used for decoding the barcode image                                                         
  * nu.pattern.opencv , Version: 2.4.9-4 -> user for processing the images,  computer vision and machine learning
  
 #To use the library you need to include it in your maven pom as below:
 
```
<dependency>
  <groupId>com.github.rshtishi</groupId>
  <artifactId>pdfBarcodesProcessor</artifactId>
  <version>1.0.2-RELEASE</version>
</dependency>

  ```
  
  #Example 1 (Extracting barcodes from a pdf, and decoding the extracted barcodes)
  
  ```
  //creating the imageProcessor instance , pdfbarcodesprocessor depends from this class and it important to be customized
  //according to your needs
  ImageProcessor imageProcessor = new OpenCVImageProcessor();
  
  //we set the ration of barcode image compared to overall image, width ratio and height ratio
  imageProcessor.setBarcodeRatioToImage(0.4, 0.6);
  
  //it is important to pass the classifier, you can download the classifier from the src/main/resources/classifier-4 folder 
  //place it in your project and after that setting in image processor instance
  imageProcessor.setClassifierPath("src/main/resources/classifier-4/cascade.xml");
  
  //creating the instance of pdf barcode processor and injecting the imageProcessor dependency     
  PdfBarcodesProcessor pdfBarcodesProcessor = new PdfBarcodesProcessorImpl.Builder()
				.imageProcessor(imageProcessor).build();
    
  String filePath = "../pdf-test.pdf";
  
  //The method processPdfBarcodesPerPage returns document containg pages with information from extracted from pdf pages.
  PdfDocument resultDoc = pdfBarcodesProcessor.processPdfBarcodesPerPage(filePath); 
  
  //return the total number of pdf pages
  int totalPagesNo = resultDoc.getTotalPages();
  
  //save all decoded bar code images from pdf into directory that you specify as argument
  //the image name for decoded barcode would be the decoded barcode text
  resultDoc.saveAllDecodedBarcodeImages("../dirPath");
  
  //returns the list with all pdf pages
  List<PdfPage> pageList = resultDoc.getPdfPageList();
  
  PdfPage page1 = pageList.get(0);
  //Will save all decoded barcode images of pdf page in directory that you specify.
  //The name of image will be the decoded barcode text.
  page1.saveDecodedBarcodeImages("../dirPath");
  
  //Will return all the images extracted from pdf pages
  page1.getImages();
  
  //will return the page name of pdf page
  //the page name = pdf name + page_+page no
  page1.getPageName()
  
  //will return list of images for which the barcode was decoded
  page1.getAllDecodedBarcodeImages();
  
  //will return the list of text extracted from barcode images
  page1.getAllDecodedBarcodesList()
  
  ```
  
  Example 2 (Simplest way to create an instance of pdfBarcodeprocessor)
  
  ```
  //It is required that image processor to be always customized, you need to set path of classifier
  //till now the folder classifier-4 contains the best trained classifier, but if you want to train the classifier
  //by yourself 
  // /pdf-barcodes-processor/src/test/java/al/tirana/pdfBarcodesProcessor/imageProcessor/OpenCVTrainingClassifierTest.java       
  //class will help you generate the data for training the classifier
  ImageProcessor imageProcessor = new OpenCVImageProcessor();
  imageProcessor.setClassifierPath("src/main/resources/classifier-4/cascade.xml");
   
   //file path of pdf file which we want to process
  	String filePath = "src/main/resources/test.pdf";
   //the result after processing the pdf file
		 PdfDocument resultDoc = pdfBarcodesProcessor.processPdfBarcodesPerPage(filePath);
  
  ```
  
  Example 3 (Customizing image processor, pdf processor and barcode decoder)
  
  ```
  //creating the image processor and customizing to our needs, 
  //It is need to be passed explicitely
  ImageProcessor imageProcessor = new OpenCVImageProcessor();
		imageProcessor.setBarcodeRatioToImage(0.4, 0.6);
  //THis is property which is required to passed in order for the library to work, the classifier defines the 
  //sucessof of the library in finding barcodes in images extracted from pdf
		imageProcessor.setClassifierPath("src/main/resources/classifier-4/cascade.xml");
  
  //it is not required to be passed explicitely
  PdfProcessor pdfProcessor = new PdfBoxPdfProcessor();
  //it is not required to be passed explicitely
  BarcodeDecoder barcodeDecoder = new ZxingBarcodeDecoder();
  
  //We create the pdf barcode processor instance
  PdfBarcodesProcessor pdfBarcodesProcessor = new PdfBarcodesProcessorImpl.Builder()
				.imageProcessor(imageProcessor)
    .pdfProcessor(pdfProcessor)
    .barcodeDecoder(barcodeDecoder)
    .build();
    
    
String filePath = "src/main/resources/test.pdf";

PdfDocument resultDoc = pdfBarcodesProcessor.processPdfBarcodesPerPage(filePath);
  
  ```
  
  #### PDFBOX
  I have used pdfbox to extract the images from pdf page. Depends how pdf it created. If the all content in pdf is an image,
  than the image extracted would be the whole pdf page. If the pdf containing images and text, than from the pdf page would 
  be extracted only the images.
  
  #### OPENCV
  I have used opencv for image processing, machine learning and computer vision. With opencv I trained a classifer to recognized 
  images that have barcode. In this release classifier works if image contain only one barcode  and is aligned horizontally. It 
  recognizes even if it was aligned vertically or askew but the cropping area it not very accurate. In future release I will 
  better classifier with a larger subset of data. OpenCv i have used also to perform operation on the image like cropping an area
  or rotating an images. 
  
  #### ZXING
  I have used zxing library to decode the barcode images. It is the best option for open source barcode decoder libraies t'ill now.
  
  
  
