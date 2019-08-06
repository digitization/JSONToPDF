package com.perfios.JSONToPDFV2;

import java.util.Comparator;

public class Word implements Comparable<Word>{
	private String value;
	private float x;
	private float y;
	private float w;
	private float h;
	
	public Word(String value, float x, float y, float w, float h) {
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
	@Override
	public String toString() {
		return "Word [value=" + value + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}
	public float getH() {
		return h;
	}
	public void setH(float h) {
		this.h = h;
	}

	public int compareTo(Word o) {
		return (int) (this.getY() - o.getY());
	}

	public static Comparator<Word> WordNameComparator 
	= new Comparator<Word>() {

		public int compare(Word word1, Word word2) {
			return word1.compareTo(word2);

		}
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(h);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (Float.floatToIntBits(h) != Float.floatToIntBits(other.h))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	
}


