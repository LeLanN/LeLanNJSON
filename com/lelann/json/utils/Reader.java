package com.lelann.json.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class Reader  implements Iterator<String>{
	private int line = 1, column = 0, cursor = 0;
	private char[] content;
	private boolean isString = false, isEchap = false, isOpen = false;
	
	public int getCursor(){
		return this.cursor;
	}
	public String getContent(){
		return new String(content);
	}
	public Reader(String content){
		this.content = content.toCharArray();
	}
	public boolean hasNext(){
		return (content.length > cursor);
	}
	public String next(){
		String charac = "";
		boolean isCharacFinded = false;
		while(!isCharacFinded){
			if(content.length == cursor) return "";
			charac = new Character(content[cursor]).toString();
			if(charac.equals("\n")){
				line++;
				column = 0;
			} else column++;

			if(charac.equals("\n") || charac.equals("\t") || charac.equals(" ")){
				if(isString){
					isCharacFinded = true;
					charac = " ";
				}
			} else {
				if(charac.equals("\"") && !isString)
					isString = true;
				else if(charac.equals("\"") && isString && !isEchap)
					isString = false;
				else if(charac.equals("\\")){
					isEchap = !isEchap;
				} else if(isString) isEchap = false;
				isCharacFinded = true;
			}
			cursor++;
			
			if(!charac.equals("{") && !isOpen) isCharacFinded = false;
			else isOpen = true;
		}
		return charac;
	}
	public void previous(){
		cursor--;
	}
	public void setString(boolean isString){
		this.isString = isString;
	}
	public boolean isString(){
		return this.isString;
	}
	public int getColumn(){
		return column;
	}
	public int getLine(){
		return line;
	}
	public void remove(){
		cursor = 0;
	}
	
	public JCharacter[] parse(){
		ArrayList<JCharacter> back = new ArrayList<JCharacter>();
		
			while(hasNext()){
				String next = next();
				if(!next.equals("")){
					back.add(new JCharacter(next, column, line));
				}
			}
			
		return back.toArray(new JCharacter[0]);
	}
}
