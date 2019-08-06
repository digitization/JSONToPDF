package com.perfios.JSONToPDFV2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Line implements Comparable<Line>{
	private String value;
	private float x;
	private float y;
	private float w;
	private float h;

	private List<Word> words;

	public Line(String value, float x, float y, float w, float h) {
		super();
		this.value = value;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void addWord(Word w) {
		if(this.words!=null) {
			this.words.add(w);
		}
		else {
			this.words=new ArrayList<Word>();
			this.words.add(w);
		}
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getW() {
		return w;
	}
	public void setW(float w) {
		this.w = w;
	}
	public float getH() {
		return h;
	}
	public void setH(float h) {
		this.h = h;
	}


	public List<Word> getWords() {
		return words;
	}
	public void setWords(List<Word> words) {
		this.words = words;
	}
	
	public int compareTo(Line o) {
		return (int) (this.getY() - o.getY());

	}

	public static Comparator<Line> lineNameComparator 
	= new Comparator<Line>() {

		public int compare(Line line1, Line line2) {
			return line1.compareTo(line2);

		}

	};

	@Override
	public String toString() {
		return "Line [value=" + value + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ",\n words=" + words + "]\n-----------------------------------------------------------------------\n";
	}
	
	


}
