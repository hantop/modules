package com.fenlibao.p2p.util.loader;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.fenlibao.p2p.util.api.load.ApiUtilConfig.loadUtilConfig;

public class Config {
	
	private static final String CONFIG_PROPERTIES = "config.properties";
//	private static final String METHOD_XML = "method.xml";

	private static Properties props = new Properties();
	private static List<String> methods = new ArrayList<String>();
	
	public static void load() {
		Config config = new Config();
		config.loadProperties();
		loadUtilConfig();
//		config.loadXmls();
	}

	private void loadProperties() {
		InputStreamReader input = null;
		try {
			input = new InputStreamReader(
					Config.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES), "UTF-8");
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

//	private void loadXmls() {
//		InputStream input = null;
//		try {
//			input = Config.class.getClassLoader().getResourceAsStream(METHOD_XML);
//			SAXParserFactory factory = SAXParserFactory.newInstance();
//			SAXParser parser = factory.newSAXParser();
//			SaxHandler handler = new SaxHandler();
//			parser.parse(input, handler);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	class SaxHandler extends DefaultHandler {

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
		}

		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String value = new String(ch, start, length);
			if (StringUtils.isNotBlank(value)) {
				methods.add(value);
			}
			super.characters(ch, start, length);
		}

	}

	public static List<String> getMethods() {
		return methods;
	}

	public static boolean isExcludeUrl(String method) {
		return methods.contains(method);
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
