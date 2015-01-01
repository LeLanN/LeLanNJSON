package com.lelann.json.utils;

import com.lelann.json.utils.JCharacter;
/**
 * Exception when loading JSON
 */
public class JSyntaxException extends Exception{
	private static final long serialVersionUID = 8986722497491664926L;
	/**
	 * 
	 * @param carac
	 * 		   The corresponding JSONCharacter
	 */
	public JSyntaxException(JCharacter carac){
		super("Unexpected character '" + carac.getValue() + "' at line " + carac.getLine() + ", column " + carac.getColumn() + ".");
	}
}
