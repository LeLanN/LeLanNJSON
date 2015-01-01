package com.lelann.json.element;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.lelann.json.JSON;
import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.JSyntaxException;

/**
 * Represent a JSON Element (variable, object, array)
 * @author LeLanN
 *
 */
public abstract class JElement {
	/**
	 * Load the JElement with a JCharacter Array
	 * @param caracs
	 * 		The JCharacter Array
	 * @throws JSyntaxException
	 */
	public abstract void load(JCharacter[] caracs) throws JSyntaxException;
	/**
	 * Return the String value of the JElement
	 * @return
	 * 		The String
	 */
	public abstract String getValue();
	/**
	 * Return the value of the JElement with an increment (used only for JArray and JObject)
	 * @param increment
	 * 		The increment
	 * @return
	 * 		The Value as a String
	 */
	public abstract String storable(int increment);
	/**
	 * Check if the JElement is empty
	 */
	public abstract boolean isEmpty();

	/**
	 * Get an Object with his Class and an JElement
	 * @param type
	 * 		The Class of the Object
	 * @param e
	 * 		The JElement
	 * @param f
	 * 		The Field of this Object in a Class
	 * @return
	 * 		The new instance of this object (can be null)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getFromElement(Class<?> type, JElement e, Field f){
		if(e instanceof JArray){
			return ((JArray) e).get(type, f);
		} else if(e instanceof JObject){
			return JSON.saveAsObject((JObject) e, type);
		} else if(e instanceof JVar){
			if(type.isEnum()){
				try{
					return Enum.valueOf((Class<Enum>)type, ((JVar) e).getStringValue());
				} catch(IllegalArgumentException exc){
					exc.printStackTrace();
				}
			}
			return ((JVar) e).getValue(type);
		}
		return null;
	}
	/**
	 * Get a JElement with an Object
	 * @param o
	 * 		The instance of this Object
	 * @return
	 * 		The new JElement
	 */
	public static JElement getFromObject(Object o){
		JElement element = null;
		if(o == null) return element;
		if(JElementLoader.getLoader(o.getClass()) != null){
			return JElementLoader.getLoader(o.getClass()).save(o);
		}
		if(o instanceof String){
			element = new JVar((String) o);
		} else if(o instanceof Number){
			element = new JVar(((Number) o).toString());
		} else if(o instanceof List){
			element = new JArray((List<?>) o);
		} else if(o instanceof Boolean){
			element = new JVar(((Boolean)o).toString());
		} else if(o.getClass().isArray()){
			element = new JArray(o);
		} else if(o.getClass().isEnum()){
			element = new JVar(o.toString());
		} else if(o instanceof Map<?,?>){
			element = new JObject((Map<?,?>)o);
		} else if(o instanceof JElement){
			element = (JElement)o;
		} else if(JElementLoader.getLoader(o.getClass()) != null){
			element = JElementLoader.getLoader(o.getClass()).save(o);
		} else {
			element = JSON.loadFromObject(o);
		}
		
		return element;
	}
	@Override
	public String toString(){
		return getValue().toString();
	}
}
