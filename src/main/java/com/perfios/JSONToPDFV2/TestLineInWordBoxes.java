package com.perfios.JSONToPDFV2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONObject;

public class TestLineInWordBoxes {
	
	public static void main(String[] args) throws IOException {
		File file = new File("/home/aniruddha/digitization/JSON/basenames");
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st; 
		while ((st = br.readLine()) != null) {
			System.out.println(st); 
			float xcoordinateLine,ycoordinateLine;
			float wcoordinateLine,hcoordinateLine;
			float xcoordinateWord,xcoordinateWordNet;
			float ycoordinateWord,wcoordinateWord,hcoordinateWord;
			float xcoordinateLineNet=0,ycoordinateLineNet=0;
			float imageHeight;
			float imageWidth;
			float pageHeight,pageWidth;
			String valueLine,valueWord;
			List<Line> listOfLines=new ArrayList<Line>();
			Set<Word> setOfWords=new HashSet<Word>();
			List<Word> listOfWords;
			//Obtaining JSON from file
			JSONObject jsonObj=getJSONObjectFromFilePath("/home/aniruddha/digitization/JSON/json_file/"+st);
			imageHeight=jsonObj.getFloat("height");
			imageWidth=jsonObj.getFloat("width");
			//Create a new PDF Document
			PDDocument document = new PDDocument();
			
			//Creating the pages of the document 
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);
			pageWidth=page.getMediaBox().getWidth();
			pageHeight=page.getMediaBox().getHeight();
			PDPageContentStream contentStream = new PDPageContentStream(document,page);
			
			for(int lineIndex=1;lineIndex<jsonObj.length()-2;lineIndex++) {
				xcoordinateLine = jsonObj.getJSONObject(Integer.toString(lineIndex)).getFloat("x");
				ycoordinateLine = jsonObj.getJSONObject(Integer.toString(lineIndex)).getFloat("y");
				wcoordinateLine = jsonObj.getJSONObject(Integer.toString(lineIndex)).getFloat("w");
				hcoordinateLine = jsonObj.getJSONObject(Integer.toString(lineIndex)).getFloat("h");
				valueLine = jsonObj.getJSONObject(Integer.toString(lineIndex)).getString("Value");
				valueLine = valueLine.replace("\n", "").replace("\r", "");
				
				Line line = new Line(valueLine,xcoordinateLine,ycoordinateLine,wcoordinateLine,hcoordinateLine);
				listOfLines.add(line);
				
				Set<String> keySet = jsonObj.getJSONObject(Integer.toString(lineIndex)).keySet();
				List<String> keyList;
				keyList=getNumList(keySet);
				
				for(String wordIndex:keyList) {
					xcoordinateWord = jsonObj.getJSONObject(Integer.toString(lineIndex)).getJSONObject(wordIndex).getFloat("x");
					ycoordinateWord = jsonObj.getJSONObject(Integer.toString(lineIndex)).getJSONObject(wordIndex).getFloat("y");
					wcoordinateWord = jsonObj.getJSONObject(Integer.toString(lineIndex)).getJSONObject(wordIndex).getFloat("w");
					hcoordinateWord = jsonObj.getJSONObject(Integer.toString(lineIndex)).getJSONObject(wordIndex).getFloat("h");
					valueWord= jsonObj.getJSONObject(Integer.toString(lineIndex)).getJSONObject(wordIndex).getString("Value");
					valueWord = valueWord.replace("\n", "").replace("\r", "");
					
					Word eachWord = new Word(valueWord,xcoordinateWord,ycoordinateWord,wcoordinateWord,hcoordinateWord);
					setOfWords.add(eachWord);
					eachWord.toString();
				}
			}
			Collections.sort(listOfLines);
			listOfWords = new ArrayList<Word>(setOfWords);
			Collections.sort(listOfWords);
			float smallest=(float) 0.0;
			int saveIndex=0;
			for(Word w:listOfWords) {
				for(int i=0;i<listOfLines.size();i++) {
					float diff=Math.abs(w.getY()-listOfLines.get(i).getY());
					if(i==0) {
						smallest=diff;
						saveIndex=i;
						continue;
					}
					if(diff <= smallest) {
						smallest = diff;
						saveIndex=i;
					}
					else {
						listOfLines.get(saveIndex).addWord(w);
						break;
					}
				}
			}
			
			for(Line l:listOfLines) {
				System.out.println(l.toString());
			}
			
			for(Line l:listOfLines) {
				if(l.getWords()==null) {
					continue;
				}
				for(Word w:l.getWords()) {
					writeWordAtCoordinates(document, page, contentStream, w.getValue(), scale(w.getX(), 0, imageWidth, 0, pageWidth), pageHeight-scale(l.getY(), 0, imageHeight, 0, pageHeight));
				}
			}
			
			System.out.println("Page height:"+pageHeight); 
			System.out.println("Page width:"+pageWidth); 
			System.out.println("Content added"); 
			contentStream.close();
			//Saving the document 
			document.save(new File("/home/aniruddha/digitization/pdfOutputs/"+st+".pdf")); 
			//Closing the document 
			document.close();
		}
	}
	
	public static float scale(float x,float old_min, float old_max,float new_min, float new_max){
		float old_range = old_max - old_min;
		float new_range = new_max - new_min;
		return Math.abs((new_range/old_range))*x;
		//		return new_min + (x - old_min) * new_range / old_range;
	}
	
	private static boolean checkWhetherToPrintWord(String word,String line,Set<String> setOfWords) {
		if(line.contains(word)){
			return true;
		}
		else {
			if(setOfWords.contains(word)) {
				return false;
			}
		}
		return true;
	}
	
	private int fontAdjuster(float height,float width) {
		for(int i=1;i<13;i++) {
			PDFont font = PDType1Font.TIMES_ROMAN;
			System.out.println(font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * i);
			
		}
		return 0;
	}
	
	private static void writeWordAtCoordinates(PDDocument document,PDPage page,PDPageContentStream contentStream,String text,float x,float y) throws IOException {
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
	
	private static List<String> getNumList(Set<String> keys){
		List<String> numList=new ArrayList<String>();
		for(String key:keys) {
			if(key.matches("-?\\d+(\\.\\d+)?")) {
				numList.add(key);
			}
		}
		return numList;
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
}