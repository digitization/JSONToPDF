package com.perfios.JSONToPDFV2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	float xcoordinateLine,ycoordinateLine;
		String valueLine,valueWord;
    	JSONObject jsonObjLine=getJSONObjectFromFilePath("/home/akshay/digitization/JSON/value2.json");
    	PDDocument document = new PDDocument();

		//Creating the pages of the document 
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);
		PDPageContentStream contentStream = new PDPageContentStream(document,page);
		for(int lineIndex=1;lineIndex<jsonObjLine.length();lineIndex++) {
			xcoordinateLine = jsonObjLine.getJSONObject(Integer.toString(lineIndex)).getFloat("x1");
			ycoordinateLine = jsonObjLine.getJSONObject(Integer.toString(lineIndex)).getFloat("x2");
			valueLine = jsonObjLine.getJSONObject(Integer.toString(lineIndex)).getString("value");
			writeWordAtCoordinates(document,page,contentStream,valueLine,xcoordinateLine,ycoordinateLine);

		}
		contentStream.close();
		document.save(new File("/home/akshay/digitization/pdfOutputs/test.pdf")); 
		//Closing the document 
		document.close();
    }
    private static String readAllBytesJava7(String filePath)
	{
		String content = "";

		try
		{
			content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return content;
	}


	private static JSONObject getJSONObjectFromFilePath(String filePath) {

		return new JSONObject(readAllBytesJava7(filePath)); 
	}
	private static void writeWordAtCoordinates(PDDocument document,PDPage page,PDPageContentStream contentStream,String text,float x,float y) throws IOException{
		//Begin the Content stream 
		contentStream.beginText(); 

		//Setting the font to the Content stream  
		contentStream.setFont(PDType1Font.TIMES_ROMAN, 4);

		//Setting the position for the line 
		contentStream.newLineAtOffset(x,y);

		//Adding text in the form of string 
		contentStream.showText(text);      

		//Ending the content stream
		contentStream.endText();
	}
}
