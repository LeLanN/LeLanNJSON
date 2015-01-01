package com.lelann.json.element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lelann.json.JSON;
import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.JData;
import com.lelann.json.utils.JSyntaxException;
import com.lelann.json.utils.ReaderUtils;

/**
 * Represent a JSON Array
 * @author LeLanN
 */
public class JArray extends JElement {
	private List<JElement> vars = new ArrayList<JElement>();


	public List<JElement> getValues(){
		return this.vars;
	}
	public JArray(JCharacter[] caracs) throws JSyntaxException{
		load(caracs);
	}
	public JArray(List<?> value) {
		for(Object o : value){
			vars.add(JElement.getFromObject(o));
		}
	}
	public JArray(Object what){
		if(what.getClass().isArray()){
			for(Object o : convertToObjectArray(what)){
				vars.add(JElement.getFromObject(o));
			}
		}
	}
	@Override
	public void load(JCharacter[] datas) throws JSyntaxException{
		boolean isString = false, isEchap = false;
		String current = "";

		JData wdata = JData.DATA_VALUE;
		for(int i=0;i<datas.length;i++){
			final JCharacter c = datas[i];
			final char ch = c.getValue().toCharArray()[0];
			if(isString){
				if(c.getValue().equals("\\")){
					isEchap = !isEchap;
				} else if(c.getValue().equals("\"") && !isEchap){
					isString = false;
					if(wdata == JData.DATA_VALUE){
						wdata = JData.NEXT_DATA;
						vars.add(new JVar(current));
						current = "";
					} else {
						throw new JSyntaxException(c);
					}
					continue;
				} else {
					isEchap = false;
				}
				current += c.getValue();
			} else if(c.getValue().equals("\"")){
				if(wdata == JData.DATA_VALUE){
					isString = true;
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals("{")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullObject(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;

					vars.add(new JObject(nDatas));
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals(",")){
				if(wdata == JData.NEXT_DATA){
					wdata = JData.DATA_VALUE;
				} else if(wdata == JData.VAR_NBR){
					wdata = JData.DATA_VALUE;
					vars.add(new JVar(current));
					current = "";
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals("[")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullArray(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;

					vars.add(new JArray(nDatas));
				} else {
					throw new JSyntaxException(c);
				}
			} else if(Character.isDigit(ch)){
				if(wdata == JData.DATA_VALUE || wdata == JData.VAR_NBR){
					wdata = JData.VAR_NBR;
					current += c.getValue();
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals(".")){
				if(wdata == JData.VAR_NBR){
					wdata = JData.VAR_NBR;
					current += c.getValue();
				} else {
					throw new JSyntaxException(c);
				}
			} else {
				throw new JSyntaxException(c);
			}
		}
		if(wdata == JData.VAR_NBR){
			vars.add(new JVar(current));
		}
	}
	@Override
	public String storable(int incremente) {
		String inc = "", result = "";
		for(int i=0;i<incremente;i++){
			inc += " ";
		}
		result += "[\n";
		String valueInc = inc +  "  ";

		boolean isFirst = true;
		for(final JElement key : vars){
			if(!isFirst) result += ",\n"; else isFirst = false;
			result += valueInc + key.storable(incremente + 2);
		}

		result += "\n" + inc + "]";
		return result;
	}
	/**
	 * Get the Array as a String. The text will contains spaces, lines, ...
	 */
	public String storable(){
		return storable(0);
	}
	@Override
	public String getValue() {
		String result = "[";

		boolean isFirst = true;
		for(final JElement key : vars){
			if(!isFirst) result += ","; else isFirst = false;
			result += key;
		}
		result += "]";
		return result;
	}
	/**
	 * Useful for the API to convert an Object to an Array of this Object.
	 * The method is accessible, because she can be useful in many case.
	 * @param array
	 */
	public static Object[] convertToObjectArray(Object array) {
		Class<?> ofArray = array.getClass().getComponentType();
		if (ofArray.isPrimitive()) {
			List<Object> ar = new ArrayList<Object>();
			int length = Array.getLength(array);
			for (int i = 0; i < length; i++) {
				ar.add(Array.get(array, i));
			}
			return ar.toArray();
		}
		else {
			return (Object[]) array;
		}
	}
	@Override
	public boolean isEmpty(){
		return vars.isEmpty();
	}
	/**
	 * Add an Object to this Array
	 * @param o The Object to add
	 */
	public void add(Object o){
		JElement je = JElement.getFromObject(o);
		if(je != null) vars.add(je);
	}
	public List<Boolean> getBooleanList(){
		List<Boolean> o = new ArrayList<Boolean>();
		for(final JElement je : vars){
			if(je instanceof JVar)
				o.add(((JVar) je).getBooleanValue());
		}
		return o;
	}
	/**
	 *  Get a Integer List. Default value is 0
	 */
	public List<Integer> getIntList(){
		List<Integer> o = new ArrayList<Integer>();
		for(final JElement je : vars){
			if(je instanceof JVar)
				o.add(((JVar) je).getIntValue());
		}
		return o;
	}
	/**
	 *  Get a Double List. Default value is 0
	 */
	public List<Double> getDoubleList(){
		List<Double> o = new ArrayList<Double>();
		for(final JElement je : vars){
			if(je instanceof JVar)
				o.add(((JVar) je).getDoubleValue());
		}
		return o;
	}
	/**
	 *  Get a Float List. Default value is 0
	 */
	public List<Float> getFloatList(){
		List<Float> o = new ArrayList<Float>();
		for(final JElement je : vars){
			if(je instanceof JVar)
				o.add(((JVar) je).getFloatValue());
		}
		return o;
	}
	/**
	 *  Get a Short List. Default value is 0
	 */
	public List<Short> getShortList(){
		List<Short> o = new ArrayList<Short>();
		for(final JElement je : vars){
			if(je instanceof JVar)
				o.add(((JVar) je).getShortValue());
		}
		return o;
	}
	/**
	 *  Get a String List
	 */
	public List<String> getStringList(){
		List<String> o = new ArrayList<String>();
		for(final JElement je : vars){
			if(je instanceof JVar)
				o.add(((JVar) je).getStringValue());
		}
		return o;
	}
	/**
	 *  Get a JSONArray List
	 */
	public List<JArray> getArrayList(){
		List<JArray> o = new ArrayList<JArray>();
		for(final JElement je : vars){
			if(je instanceof JArray)
				o.add((JArray) je);
		}
		return o;	
	}
	/**
	 *  Get a JSONObject List
	 */
	public List<JObject> getObjectList(){
		List<JObject> o = new ArrayList<JObject>();
		for(final JElement je : vars){
			if(je instanceof JObject)
				o.add((JObject) je);
		}
		return o;
	}
	/**
	 *  Get a Object List, with all values
	 */
	public List<JElement> getList(){
		return vars;
	}
	/**
	 * Get a List of all Object of Class type. JObject will be
	 * converted as the Object if it isn't a default Class 
	 * @param type The Class (for example, Object.class)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> type){
		if(type.equals(Double.class)){
			return (List<T>)getDoubleList();
		} else if(type.equals(Short.class)){
			return (List<T>)getShortList();
		} else if(type.equals(Float.class)){
			return (List<T>)getFloatList();
		} else if(type.equals(Integer.class)){
			return (List<T>)getIntList();
		} else if(type.equals(Short.class)){
			return (List<T>)getShortList();
		} else if(type.equals(String.class)){
			return (List<T>)getStringList();
		} else if(type.equals(JArray.class)){
			return (List<T>)getArrayList();
		} else if(type.equals(JObject.class)){
			return (List<T>)getObjectList();
		} else if(type.equals(Boolean.class)){
			return (List<T>)getBooleanList();
		}
		List<T> values = new ArrayList<T>();
		for(JElement je : vars){
			T v = JSON.saveAsObject((JObject)je, type);
			values.add(v);
		}
		return values;
	}
	protected Object get(Class<?> type, Field f){
		if(type.isArray()){
			try {
				Class<?> theClass = Class.forName(type.getName().replace("[L", "").replace(";", ""));
				List<?> obj = getList(theClass);
				Object[] o = (Object[])type.cast(Array.newInstance(theClass, obj.size()));
				for(int i=0;i<obj.size();i++){
					o[i] = obj.get(i);
				}
				return o;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if(type.equals(List.class) || Arrays.asList(type.getInterfaces()).contains(List.class)){
			return getList((Class<?>)((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);
		}
		return null;
	}
}
