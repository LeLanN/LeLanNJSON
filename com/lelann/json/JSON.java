package com.lelann.json;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;

import com.lelann.json.element.JElement;
import com.lelann.json.element.JElementLoader;
import com.lelann.json.element.JObject;
import com.lelann.json.utils.FileUtils;
import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.JSyntaxException;
import com.lelann.json.utils.Reader;
import com.lelann.json.utils.ReaderUtils;


public class JSON {
	/**
	 * Load JSON from String
	 * 
	 * @param content
	 *            The JSON string
	 * 
	 * @return A new JSONObject
	 */
	public static JObject loadFromString(String content){
		JCharacter[] ca = new Reader(content).parse();
		try {
			return new JObject(ReaderUtils.getFullObject(ca, 0));
		} catch (JSyntaxException e) {
			e.printStackTrace();
			return loadFromString("{}");
		}
	}
	/**
	 * Load JSON from URL
	 * 
	 * @param url
	 *            The URL of the JSON File
	 * 
	 * @return A new JSONObject
	 */
	public static JObject loadFromUrl(String url){
		try {
			return loadFromString(FileUtils.getContent(new URL(url)));
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
		return loadFromString("{}");
	}
	/**
	 * Load JSON from File
	 * 
	 * @param file
	 *            The JSON file
	 * 
	 * @return A new JSONObject
	 */
	public static JObject load(File file){
		String content = null;
		try {
			if(!file.exists()) file.createNewFile();
			content = FileUtils.getContent(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loadFromString(content);
	}
	/**
	 * Load a JObject from an Object
	 * @param o
	 * 		The Object
	 * @return
	 * 		A new JObject
	 */
	public static JObject loadFromObject(Object o){
		if(JElementLoader.getLoader(o.getClass()) != null){
			return JElementLoader.getLoader(o.getClass()).save(o);
		}
		JObject jo = JSON.loadFromString("{}");
		for(Field f : o.getClass().getDeclaredFields()){
			if((f.getModifiers() & 0x00000008) != 0 || f.getClass().getName().equals(o.getClass().getName())){
				continue;
			}
			try {
				f.setAccessible(true);
				jo.set(f.getName(), JElement.getFromObject(f.get(o)));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return jo;
	}
	@SuppressWarnings("unchecked")
	private static <T> T createObjectInstance(Class<T> c){
		if(c.isEnum() || c.isArray()){ // Could not load an Enum or an Array from an JObject
			return null;
		} else if(c.isInterface()){

		} else {
			try{
				return getConstructor(c).newInstance();
			} catch(Exception e){}
			try {
				Class<?> unsafe = Class.forName("sun.misc.Unsafe");
				Field f = unsafe.getDeclaredField("theUnsafe"); f.setAccessible(true);
				return (T) unsafe.getMethod("allocateInstance", Class.class).invoke(f.get(null), c);
			} catch (Exception e){}
		}
		return null;
	}
	/**
	 * Create an Object (an instance of the specified Class) from a JObject
	 * @param jo The JObject
	 * @param c The Class
	 */
	@SuppressWarnings("unchecked")
	public static <T> T saveAsObject(JObject jo, Class<T> c){
		if(JElementLoader.getLoader(c) != null){
			T o = (T) JElementLoader.getLoader(c).load(jo);
			if(o != null) return o;
		}
		T o = createObjectInstance(c);
		if(o == null) return null;
		
		for(Field f : o.getClass().getDeclaredFields()){
			if((f.getModifiers() & 0x00000008) != 0){
				continue;
			}
			JElement newValue = jo.get(f.getName());
			if(newValue == null) continue;

			try {
				f.setAccessible(true);
				Object value = JElement.getFromElement(f.getType(), newValue, f);
				if(value != null){
					f.set(o, value);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return o;
	}
	private static <T> Constructor<T> getConstructor(Class<T> c){
		try {
			return c.getDeclaredConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Can not get the default constructor of " + c.getSimpleName() + ".", e);
		}
	}
	/**
	 * Save a JObject as a File
	 * @param file
	 * 		The File
	 * @param o
	 * 		The JObject to save
	 */
	public static void save(File file, JObject o){
		try {
			FileUtils.save(o.storable(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
