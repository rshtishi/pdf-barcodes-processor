# pdf-barcodes-processor
A library that offers the functionalities for processing bar codes. It works with Java version 1.8+.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.rshtishi/pdfBarcodesProcessor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.rshtishi/pdfBarcodesProcessor)

The link below directs to a test project in GitHub that shows an example of using the pdf-barcodes-processor library.   
[Project Test Example Of Using Pdf Barcode Processor](https://github.com/rshtishi/pdf-barcodes-processor-test)

*pdf-barcodes-processor* inlcudes the following dependencies:

  * org.apache.pdfbox [version: 2.0.16] (used for processing the pdf, extracting pdf page, and extracting images from pdf page)
  * com.google.zxing , [version: 3.3.3] (used for decoding the barcode image)                                                         
  * nu.pattern.opencv , [Version: 2.4.9-] (user for processing the images,  computer vision and machine learning)
  
 #### To use the library you need to include it in your maven pom as below:
 
```
<dependency>
  <groupId>com.github.rshtishi</groupId>
  <artifactId>pdfBarcodesProcessor</artifactId>
  <version>1.0.2-RELEASE</version>
</dependency>

  ```
  
  ## Example 1 (Extracting barcodes from a pdf, and decoding the extracted barcodes)
  
  ```
  //creating the imageProcessor instance , pdfbarcodesprocessor depends on this class and it important 
  // to be customized according to your needs
  ImageProcessor imageProcessor = new OpenCVImageProcessor();
  
  //we set the ratio of barcode image with an image containing the barcode image, width ratio and height ratio
  imageProcessor.setBarcodeRatioToImage(0.4, 0.6);
  
  //it is important to pass the classifier, you can download the classifier from the src/main/resources/classifier-4 folder 
  //place it in your project and after that set classifierPath property in image processor instance
  imageProcessor.setClassifierPath("src/main/resources/classifier-4/cascade.xml");
  
  //creating the instance of pdf barcode processor and injecting the imageProcessor dependency     
  PdfBarcodesProcessor pdfBarcodesProcessor = new PdfBarcodesProcessorImpl.Builder()
				.imageProcessor(imageProcessor).build();
    
  String filePath = "../pdf-test.pdf";
  
  //The method processPdfBarcodesPerPage returns document containing pages with information extracted from pdf pages.
  PdfDocument resultDoc = pdfBarcodesProcessor.processPdfBarcodesPerPage(filePath); 
  
  //return the total number of pdf pages
  int totalPagesNo = resultDoc.getTotalPages();
  
  //save all decoded bar code images from pdf into the directory that you passed as an argument
  //the image name for decoded barcode would be the decoded barcode text
  resultDoc.saveAllDecodedBarcodeImages("../dirPath");
  
  //returns the list with all pdf pages
  List<PdfPage> pageList = resultDoc.getPdfPageList();
  
  PdfPage page1 = pageList.get(0);
  //Will save all decoded barcode images of pdf page in the directory that you specify.
  //The name of the image will be the decoded barcode text.
  page1.saveDecodedBarcodeImages("../dirPath");
  
  //Will return all the images extracted from pdf pages
  page1.getImages();
  
  //will return the page name of pdf page
  //the page name = pdf name + page_+page no
  page1.getPageName()
  
  //will return a list of images for which the barcode was decoded
  page1.getAllDecodedBarcodeImages();
  
  //will return the list of text extracted from barcode images
  page1.getAllDecodedBarcodesList()
  
  ```
  
  ## Example 2 (Simplest way to create an instance of pdfBarcodeprocessor)
  
  ```
  //It is required that image processor to be always customized, you need to set path of classifier
  //till now the folder classifier-4 contains the best-trained classifier, but if you want to train the classifier
  //by yourself 
  // /pdf-barcodes-processor/src/test/java/al/tirana/pdfBarcodesProcessor/imageProcessor/OpenCVTrainingClassifierTest.java       
  //class will help you generate the data for training the classifier
  ImageProcessor imageProcessor = new OpenCVImageProcessor();
  imageProcessor.setClassifierPath("src/main/resources/classifier-4/cascade.xml");
   
   //file path of the pdf file which we want to process
   String filePath = "src/main/resources/test.pdf";
   
   //the result after processing the pdf file
   PdfDocument resultDoc = pdfBarcodesProcessor.processPdfBarcodesPerPage(filePath);
  
  ```
  
  ## Example 3 (Customizing image processor, pdf processor and barcode decoder)
  
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
  I have used pdfbox to extract the images from the pdf page. If the content in pdf is an image then the image extracted would be the whole pdf page. If the pdf content contains images and text, then from the pdf page would be extracted only the images.
  
  #### OPENCV
  I have used OpenCV for image processing, machine learning, and computer vision. With OpenCV I trained a classifier to recognize images that have barcodes. This release classifier works if the image contains only one barcode and is aligned horizontally. It recognizes even if it was aligned vertically or askew but the cropping area is not very accurate. In a future release, I will train a classifier with a larger subset of data. OpenCV library is also used to operate on the image like cropping, rotating, etc. 
  
  #### ZXING
  I have used the zxing library to decode the barcode images. It is the best option for open-source barcode decoder libraries that I could find.
  
  
  
