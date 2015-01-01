package com.lelann.json.utils;

/**
 * Represent a Character of a File
 * @author Moi
 */
public class JCharacter {
	private String value;
	private int column, line;
	
	/**
	 * Get the column of the character
	 */
	public int getColumn(){
		return this.column;
	}
	/**
	 * Get the line of the character
	 */
	public int getLine(){
		return this.line;
	}
	/**
	 * Get the value of the character
	 */
	public String getValue(){
		return this.value;
	}
	
	public JCharacter(String value, int column, int line){
		this.value = value;
		this.column = column;
		this.line = line;
	}
}
