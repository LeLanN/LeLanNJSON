package com.lelann.json.element;

import java.util.HashMap;
import java.util.Map;
/**
 * Manage Object loading and saving
 * @author Moi
 *
 */
public abstract class JElementLoader {
	private static Map<String, JElementLoader> loaders = new HashMap<String, JElementLoader>();
	/**
	 * Set a JElementLoader instance for a Class and his subclasses
	 * @param clazz The Class
	 * @param loader The JElementLoader
	 */
	public static void setLoader(Class<?> clazz, JElementLoader loader){
		loaders.put(clazz.getName(), loader);
	}
	/**
	 * Get the JElementLoader for a specified Class
	 * @param clazz
	 *    The Class
	 */
	public static JElementLoader getLoader(Class<?> clazz){
		JElementLoader loader = loaders.get(clazz.getName());
		Class<?> clazz2 = clazz;
		while(loader == null){
			clazz2 = clazz2.getSuperclass();
			if(clazz2.equals(Object.class))
				break;
			loader = loaders.get(clazz.getName());
		}
		if(loader == null){
			for(Class<?> interfaze : clazz.getInterfaces()){
				loader = loaders.get(interfaze.getName());
				if(loader != null) break;
			}
		}
		return loader;
	}
	static {
		setLoader(Map.class, new JElementLoader(){
			public JObject save(Object o) {
				return new JObject((Map<?, ?>) o);
			}

			public Object load(JObject o) {
				return o.getValues();
			}
		  }
		);
		setLoader(JObject.class, new JElementLoader(){
			public JObject save(Object o) {
				return null;
			}

			public Object load(JObject o) {
				return o;
			}
		  }
		);
	}
	/**
	 * Load a JObject from an Object of type <T>
	 * @param o The Object
	 */
	public abstract JObject save(Object o);
	/**
	 * Get the Object from a JObject
	 * @param o The JObject
	 */
	public abstract Object load(JObject o);
}
