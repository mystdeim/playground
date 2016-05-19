package playground.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PropertiesHelper {
	
	public static final <T> T get(String name, Class<T> resultTypeClass, String file_name) {		
		Properties props = readProps(file_name);
		if (null != props && null != props.get(name)) {
			String str = props.get(name).toString();
			if (resultTypeClass.equals(Double.class)) return resultTypeClass.cast(Double.parseDouble(str));
			else if (resultTypeClass.equals(Long.class)) return resultTypeClass.cast(Long.parseLong(str));
			else if (resultTypeClass.equals(Integer.class)) return resultTypeClass.cast(Integer.parseInt(str));
			else if (resultTypeClass.equals(Boolean.class)) return resultTypeClass.cast(Boolean.parseBoolean(str));
			else if (resultTypeClass.equals(List.class)) {
				List<String> tmp_list = new ArrayList<String>();
				for (String item : Arrays.asList(str.split(","))) {
					String str_tmp = item.replaceAll("\\W", "");
					tmp_list.add(str_tmp);
				}
				return resultTypeClass.cast(tmp_list);
			}
			else if (resultTypeClass.equals(LocalDate.class)) {
				return resultTypeClass.cast(LocalDate.parse(str));
			}
			else return resultTypeClass.cast(props.get(name));
		}
		return null;
	}
	
	public static final void put(String name, Object value, String file_name) {
		if (null != value) {
			Properties props = readProps(file_name);
			if (null == props) props = new Properties();
			if (value.getClass().isArray()) {
				props.put(name, Arrays.toString((Object[]) value));
			} if (value instanceof String) {
				props.put(name, ((String) value).trim());
			} else {
				props.put(name, value.toString());
			}
			saveProps(props, file_name);
		}
	}
	
	public static final Properties readProps(String file_name) {
		Properties props = new Properties();
		Path path = Paths.get(file_name);
		if (Files.exists(path)) {
			try (FileInputStream in = new FileInputStream(file_name)) {
				props.load(in);
				return props;
			} catch (IOException e) {
		        e.printStackTrace();
	        }
		}
		return null;
	}
	
	public static final void saveProps(Properties props, String file_name) {
		try (FileOutputStream out = new FileOutputStream(file_name)) {
			props.store(out, "");
		} catch (IOException e) {
	        e.printStackTrace();
        }
	}
	
	public static final <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
	    List<T> r = new ArrayList<T>(c.size());
	    for(Object o : c) {
	    	if (clazz.equals(Integer.class)) r.add(clazz.cast(Integer.valueOf(o.toString())));
	    	else r.add(clazz.cast(o));
	    }
	    return r;
	}

}
