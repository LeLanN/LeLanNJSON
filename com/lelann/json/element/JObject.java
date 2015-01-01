package com.lelann.json.element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lelann.json.JSON;
import com.lelann.json.utils.FileUtils;
import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.JData;
import com.lelann.json.utils.JSyntaxException;
import com.lelann.json.utils.ReaderUtils;

/**
 * Represent a JSON Object
 * @author LeLanN
 *
 */
public class JObject extends JElement {
	private Map<String, JElement> vars = new LinkedHashMap<String, JElement>();

	public JObject(JCharacter[] caracs) throws JSyntaxException{
		load(caracs);
	}
	/**
	 * Load a JObject from a Map
	 * @param vars
	 * 		The Map
	 */
	public JObject(Map<?,?> vars){
		for(Object key : vars.keySet()){
			Object value = vars.get(key);
			this.vars.put(key.toString(), JElement.getFromObject(value));
		}
	}
	/**
	 * Get all values from the JObject
	 * @return
	 * 		The corresponding Map<String, JElement>
	 */
	public Map<String, JElement> getValues(){
		return this.vars;
	}
	/**
	 * Check if the JObject contains the key
	 * @param key
     * 		The key, which must be check
	 */
	public boolean contains(String key){
		String[] objects = key.split("\\.");
		JObject o = this;
		for(int i=0;i<objects.length -1;i++){
			JElement newO = o.vars.get(objects[i]);
			if(newO == null || !(o instanceof JObject)) {
				return false;
			}
			o = (JObject) newO;
		}
		return o.vars.containsKey(key);
	}
	public boolean isEmpty(){
		return vars.isEmpty();
	}
	/**
	 * 
	 * @param key
	 * 	   The key
	 * @return
	 * 	   The corresponding element (can be null)
	 */
	public JElement get(String key){
		String[] objects = key.split("\\.");
		JObject o = this;
		for(int i=0;i<objects.length -1;i++){
			JElement newO = o.vars.get(objects[i]);
			if(newO == null || !(o instanceof JObject)) {
				return null;
			}
			o = (JObject) newO;
		}
		return o.vars.get(objects[objects.length - 1]);
	}
	/**
	 * 
	 * @param what
	 * 	   The key
	 * @return
	 * 	   The corresponding object (can be null)
	 */
	public JObject getObject(String what){
		JElement obj = get(what);
		if(obj instanceof JObject)
			return ((JObject)obj);
		else return obj == null ? null : JSON.loadFromString("{}");
	}
	/**
	 * 
	 * @param what
	 * 	   The key
	 * @return
	 * 	   The corresponding array (can be null)
	 */
	public JArray getArray(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj);
		else return obj == null ? null : new JArray(new String[]{});
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching String. Can be null
	 */
	public String getString(String what){
		JElement obj = get(what);
		if(obj instanceof JVar)
			return ((JVar)obj).getStringValue();
		else return obj == null ? null : "Is not a String !";
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Integer. If null, 0
	 */
	public int getInt(String what){
		JElement obj = get(what);
		if(obj instanceof JVar)
			return ((JVar)obj).getIntValue();
		else return obj == null ? 0 : 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Double. If null, 0
	 */
	public double getDouble(String what){
		JElement obj = get(what);
		if(obj instanceof JVar)
			return ((JVar)obj).getDoubleValue();
		else return obj == null ? 0 : 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Float. If null, 0
	 */
	public float getFloat(String what){
		JElement obj = get(what);
		if(obj instanceof JVar)
			return ((JVar)obj).getFloatValue();
		else return obj == null ? 0 : 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Long. If null, 0
	 */
	public long getLong(String what){
		JElement obj = get(what);
		if(obj instanceof JVar)
			return ((JVar)obj).getLongValue();
		else return obj == null ? 0 : 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Short. If null, 0
	 */
	public int getShort(String what){
		JElement obj = get(what);
		if(obj instanceof JVar)
			return ((JVar)obj).getShortValue();
		else return obj == null ? 0 : 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Boolean. If null, false
	 */
	public boolean getBoolean(String what){
		JElement obj = get(what);
		if(obj instanceof JVar)
			return ((JVar)obj).getBooleanValue();
		else return obj == null ? null : false;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Boolean List
	 */
	public List<Boolean> getBooleanList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getBooleanList();
		else return obj == null ? null : new ArrayList<Boolean>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Integer List
	 */
	public List<Integer> getIntList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getIntList();
		else return obj == null ? null : new ArrayList<Integer>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Double List
	 */
	public List<Double> getDoubleList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getDoubleList();
		else return obj == null ? null : new ArrayList<Double>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Float List
	 */
	public List<Float> getFloatList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getFloatList();
		else return obj == null ? null : new ArrayList<Float>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Short List
	 */
	public List<Short> getShortList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getShortList();
		else return obj == null ? null : new ArrayList<Short>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching String List
	 */
	public List<String> getStringList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getStringList();
		else return obj == null ? null : new ArrayList<String>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Array List (array containing arrays)
	 */
	public List<JArray> getArrayList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getArrayList();
		else return obj == null ? null : new ArrayList<JArray>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Object List
	 */
	public List<JObject> getObjectList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getObjectList();
		else return obj == null ? null : new ArrayList<JObject>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Object List
	 */
	public List<JElement> getList(String what){
		JElement obj = get(what);
		if(obj instanceof JArray)
			return ((JArray)obj).getList();
		else return obj == null ? null : new ArrayList<JElement>();
	}
	/**
	 * Set a new value
	 * @param key
	 * 		The value key
	 * @param obj
	 * 		The value (an Object)
	 */
	public void set(String key, Object obj){
		String[] objects = key.split("\\.");
		JObject o = this;
		for(int i=0;i<objects.length -1;i++){
			JElement newO = o.vars.get(objects[i]);
			if(newO == null || !(o instanceof JObject)) {
				newO = JSON.loadFromString("{}");
				o.set(objects[i], newO);
			}
			o = (JObject) newO;
		}
		key = objects[objects.length - 1];
		if(obj != null){
			JElement je = JElement.getFromObject(obj);
			if(je != null)
				o.vars.put(key, je);
		} else {
			if(o.vars.containsKey(key))
				o.vars.remove(key);
		}
	}
	@Override
	public void load(JCharacter[] datas) throws JSyntaxException{
		vars = new LinkedHashMap<String, JElement>();
		boolean isString = false, isEchap = false;
		String name = "", current = "";

		JData wdata = JData.NEW_DATA;
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
						set(name, new JVar(current));
						current = ""; name = "";
					} else if(wdata == JData.NEW_DATA){
						wdata = JData.DATA_OPEN;
						name = current;
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
				if(wdata == JData.DATA_VALUE || wdata == JData.NEW_DATA){
					isString = true;
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals("{")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullObject(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;

					set(name, new JObject(nDatas));
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals(",")){
				if(wdata == JData.NEXT_DATA){
					wdata = JData.NEW_DATA;
				} else if(wdata == JData.VAR_NBR){
					wdata = JData.NEW_DATA;
					set(name, new JVar(current));
					current = ""; name = "";
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals(":")){
				if(wdata == JData.DATA_OPEN){
					wdata = JData.DATA_VALUE;
				} else {
					throw new JSyntaxException(c);
				}
			} else if(c.getValue().equals("[")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullArray(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;

					set(name, new JArray(nDatas));
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
			set(name, new JVar(current));
		}
	}
	@Override
	public String storable(int incremente) {
		String inc = "", result = "";
		for(int i=0;i<incremente;i++){
			inc += " ";
		}
		result += "{\n";
		String valueInc = inc +  "  ";

		boolean isFirst = true;
		for(final String key : vars.keySet()){
			JElement value = vars.get(key);
			if(!isFirst) result += ",\n"; else isFirst = false;
			result += valueInc + "\"" + key + "\": " + value.storable(incremente + 2);
		}

		result += "\n" + inc + "}";
		return result;
	}
	/**
	 * Save the JObject as a String
	 * @return
	 * 		The String value
	 */
	public String storable(){
		return storable(0);
	}
	/**
	 * Save the JObject in a File
	 * @param file
	 * 		The File
	 */
	public void save(File file){
		try {
			FileUtils.save(storable(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String getValue() {
		String result = "{";

		boolean isFirst = true;
		for(final String key : vars.keySet()){
			JElement value = vars.get(key);
			if(!isFirst) result += ","; else isFirst = false;
			result += "\"" + key + "\":" + value.getValue();
		}
		result += "}";
		return result;
	}
}
