package com.fenlibao.p2p.util.loader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Sender {

	private static Properties props = new Properties();
	
	public static void sender(){
		Sender sender=new Sender();
		sender.loadSender();
	}
	
	private void loadSender(){
		InputStreamReader input = null;
		try {
			input = new InputStreamReader(
					Config.class.getClassLoader().getResourceAsStream("sender.properties"), "UTF-8");
			props.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String get(String key) {
		return props.getProperty(key);
	}

	public static String get(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	public static Object get(Object obj) {
		return props.get(obj);
	}
}
