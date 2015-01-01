package com.lelann.json.utils;

import java.util.ArrayList;

public class ReaderUtils {
	public static JCharacter[] getFullObject(JCharacter[] data, int begin){
		return getFull(data, begin, "{", "}");
	}
	public static JCharacter[] getFullArray(JCharacter[] data, int begin){
		return getFull(data, begin, "[", "]");
	}
	public static JCharacter[] getFull(JCharacter[] data, int begin, String open, String close){
		ArrayList<JCharacter> back = new ArrayList<JCharacter>();
		boolean isString = false, isEchap = false;
		int numberOpen = 1;
		begin++;
		
		for(int i=begin;i<data.length;i++){
			String c = data[i].getValue();
			if(c.equals("\"")){
				if(!isEchap)
					isString = !isString;
				else isEchap = false;
			} else if(c.equals("\\") && isString)
				isEchap = !isEchap;
			  else if(isString)
				isEchap = false;
			else if(c.equals(open) && !isString){
				numberOpen++;
			} else if(c.equals(close) && !isString){
				numberOpen--;
				if(numberOpen == 0) break;
			}
			
			back.add(data[i]);
		}
		
		return back.toArray(new JCharacter[0]);
	}

}
