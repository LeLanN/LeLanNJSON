package com.lelann.json.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;

public class FileUtils {
	public static String getContent(File src) throws IOException{
        return getContent(getReader(src));
	}
	public static String getContent(URL url) throws IOException{
		return getContent(getReader(url));
	}
	public static String getContent(BufferedReader input) throws IOException{
		StringBuilder builder = new StringBuilder();
        
        try {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }
	    return builder.toString();
	}
	private static BufferedReader getReader(URL url) throws IOException{
		BufferedReader input = new BufferedReader(new InputStreamReader((url.openStream())));
		if(input.readLine() == null) return input;
		
		char[] chars = input.readLine().toCharArray();
		input.close();
		
		if(chars[0] == 239 && chars[1] == 187 && chars[2] == 191){
			input = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
		} else input = new BufferedReader(new InputStreamReader(url.openStream()));
		return input;
	}
	private static BufferedReader getReader(File src) throws IOException{
        if(src.length() == 0){
        	save("{}", src);
        }
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
        
		char[] chars = input.readLine().toCharArray();
		input.close();
		
		if(chars[0] == 239 && chars[1] == 187 && chars[2] == 191){
			input = new BufferedReader(new InputStreamReader(new FileInputStream(src), Charset.forName("UTF-8")));
		} else input = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
		return input;
	}
	public static void save(String content, File dest) throws IOException{
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)));
		try {
			output.write(content);
		} finally {
			output.close();
		}
	}
}
