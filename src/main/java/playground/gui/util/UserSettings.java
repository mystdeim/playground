package playground.gui.util;

import playground.helper.PropertiesHelper;

/**
 * 
 * @author Roman Novikov
 * @since  2016-01-29
 */
public class UserSettings {
	
	//Const
	
	private static final String FILE_NAME = "users.pref";
	
	// Constructor
	// -----------------------------------------------------------------------------------------------------------------
	
	public UserSettings(Object clazz) {
		prefix = getPackageStr(clazz);
	}

	// Getters and setters
	// -----------------------------------------------------------------------------------------------------------------
	
	public String get(String name) {		
		return get(name, String.class, null);
	}
	
	public String get(String name, String default_str) {		
		return get(name, String.class, default_str);
	}
	
	public <T> T get(String name, Class<T> resultTypeClass) {		
		return get(name, resultTypeClass, null);
	}

	public <T> T get(String name, Class<T> resultTypeClass, T default_value) {
		T value = PropertiesHelper.get(getFullParamName(name), resultTypeClass, FILE_NAME);
		return null != value ? value : default_value;
	}
	
	public void put(String name, Object value) {
		PropertiesHelper.put(getFullParamName(name), value, FILE_NAME);
	}

	// Properties
	// -----------------------------------------------------------------------------------------------------------------
	
	private String prefix;
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
	
	protected String getFullParamName(String name) {
		return String.format("%s.%s", prefix, name);
	}
	
	protected String getPackageStr(Object clazz) {
		StringBuilder builder = new StringBuilder();
		clazz.getClass().getName().chars().forEach(c -> {
			if (Character.isUpperCase(c) 
					&& builder.length() > 0 
					&& builder.charAt(builder.length()-1) != '.') builder.append("_");
			builder.append((char) c);
		});
		return builder.toString().toLowerCase();
	}
}
