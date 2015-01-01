package com.lelann.json.element;

import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.JSyntaxException;

/**
 * Represent a String, a Number or a Boolean
 * @author LeLanN
 *
 */
public class JVar extends JElement{
	private Number nbrValue;
	private String strValue;
	private Boolean boolValue;
	
	public JVar(JCharacter[] datas) throws JSyntaxException{
		load(datas);
	}
	public JVar(String caracs){
		load(caracs);
	}
	/**
	 * Load a JVar from a String
	 * @param toString
	 */
	public void load(String toString){
		strValue = toString;
		try {
			nbrValue = Double.parseDouble(toString);
		} catch(Exception e){
			nbrValue = 0;
		}
		try {
			boolValue = Boolean.parseBoolean(toString);
		} catch(Exception e){
			boolValue = false;
		}
	}
	@Override
	public void load(JCharacter[] caracs) throws JSyntaxException{
		String toString = "";
		for(JCharacter c : caracs){
			toString += c.getValue();
		}
		load(toString);
	}
	@Override
	public String storable(int incremente) {
		try {
			nbrValue = Double.parseDouble(strValue);
			return strValue;
		} catch(Exception e){
			return "\"" + strValue.replace("\"", "\\\"") + "\"";
		}
	}

	public String getValue() {
		return storable(0);
	}
	/**
	 * @return Get a String value of the JSONElement. Can not return null
	 */
	public String getStringValue(){
		return strValue;
	}
	/**
	 * @return Get an Integer value of the JSONElement. If it isn't a number, return 0
	 */
	public int getIntValue(){
		return nbrValue.intValue();
	}
	/**
	 * @return Get a Double value of the JSONElement. If it isn't a number, return 0
	 */
	public double getDoubleValue(){
		return nbrValue.doubleValue();
	}
	/**
	 * @return Get a Float value of the JSONElement. If it isn't a number, return 0
	 */
	public float getFloatValue(){
		return nbrValue.floatValue();
	}
	/**
	 * @return Get a Short value of the JSONElement. If it isn't a number, return 0
	 */
	public short getShortValue(){
		return nbrValue.shortValue();
	}
	/**
	 * @return Get a Long value of the JSONElement. If it isn't a number, return 0
	 */
	public long getLongValue(){
		return nbrValue.longValue();
	}
	/**
	 * @return Get a Boolean value of the JSONElement. If it isn't a boolean, return false
	 */
	public boolean getBooleanValue(){
		return boolValue;
	}
	@Override
	public boolean isEmpty(){
		return strValue.isEmpty();
	}
	/**
	 * Get a value converted into a the Class. If the Class
	 * isn't compatible with a JVar (number, boolean or string)
	 * the method will return a null pointer.
	 * @param c
	 * 	The Class
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> c){
		if(c.equals(String.class)){
			return (T) strValue;
		} else if(c.equals(boolean.class) || c.equals(Boolean.class)){
			return (T) boolValue;
		} else if(c.equals(int.class) || c.equals(Integer.class)){
			return (T)(Integer)nbrValue.intValue();
		} else if(c.equals(double.class) || c.equals(Double.class)){
			return (T)(Double)nbrValue.doubleValue();
		} else if(c.equals(long.class) || c.equals(Long.class)){
			return (T)(Long)nbrValue.longValue();
		} else if(c.equals(short.class) || c.equals(Short.class)){
			return (T)(Short)nbrValue.shortValue();
		} else if(c.equals(float.class) || c.equals(Float.class)){
			return (T)(Float)nbrValue.floatValue();
		} else return (T) strValue;
	}
}
