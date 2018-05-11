package com.fenlibao.p2p.util.dm;

import com.fenlibao.p2p.util.api.BeanMapUtils;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * XML 助手
 * Created by zcai on 2016/8/16.
 */
public class XMLHelper {

    private static final String ROOT_ELEMENT_XML = "xml";
    private static final String XML_VERSION = "1.0";
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final Map<Class<?>, Unmarshaller> messageUnmarshaller;
    private static final Map<Class<?>, Marshaller> messageMarshaller;
    static {
        messageUnmarshaller = new ConcurrentHashMap<>();
        messageMarshaller = new ConcurrentHashMap<>();
    }

    /**
     * xml流 转 bean
     * @param xmlIns xml流
     * @param clazz bean类型
     * @param <T>
     * @return
     */
    public static <T> T toBean(InputStream xmlIns, Class<T> clazz) {
        Unmarshaller unmarshaller = messageUnmarshaller.get(clazz);
        if (unmarshaller == null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
                unmarshaller = jaxbContext.createUnmarshaller();
                messageUnmarshaller.put(clazz, unmarshaller);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Source source = new StreamSource(xmlIns);
            XmlRootElement rootElement = clazz.getAnnotation(XmlRootElement.class);
            synchronized (unmarshaller) {
                if (rootElement == null
                        || rootElement.name().equals(XmlRootElement.class.getMethod("name").getDefaultValue().toString())) {
                    JAXBElement<T> jaxbElement = unmarshaller.unmarshal(source, clazz);
                    return jaxbElement.getValue();
                } else {
                    return (T) unmarshaller.unmarshal(source);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (xmlIns != null) {
                try {
                    xmlIns.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
    }

    /**
     * xml字符串 转 bean
     * @param xmlStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String xmlStr, Class<T> clazz) {
        return toBean(new ByteArrayInputStream(xmlStr.getBytes(UTF_8)), clazz);
    }

    /**
     * bean流 转 xml流
     * @param t
     * @param os
     * @param <T>
     */
    public static <T> void toXML(T t, OutputStream os) {
        Class<T> clazz = (Class<T>) t.getClass();
        Marshaller marshaller = messageMarshaller.get(clazz);
        if (marshaller == null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
                marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_ENCODING, UTF_8.name());
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//                marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
//                    public void escape(char[] ch, int start,int length, boolean isAttVal, Writer writer) throws IOException {
//                        writer.write(ch, start, length);
//                    }
//                });
                messageMarshaller.put(clazz, marshaller);
            } catch (JAXBException e) {
                throw new IllegalArgumentException(e);
            }
        }
        try {
            XmlRootElement rootElement = clazz.getAnnotation(XmlRootElement.class);
            synchronized (marshaller) {
                if (rootElement == null
                        || rootElement.name().equals(XmlRootElement.class.getMethod("name").getDefaultValue().toString())) {
                    marshaller.marshal(new JAXBElement<T>(new QName(ROOT_ELEMENT_XML), clazz, t), os);
                } else {
                    marshaller.marshal(t, os);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
    }

    /**
     * bean 转 xml
     * @param object
     * @return
     */
    public static String toXML(Object object) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        toXML(object, os);
        return new String(os.toByteArray(), UTF_8);
    }

//    public static String toXML(Object object) {
//        return toXMLByMap(object);
//    }

    public static String toXMLByMap(Object object) {
        try {
            Map<String, Object> params = BeanMapUtils.toMap(object);
            return mapToXml(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将map转成xml
     * @param map
     * @return
     */
    public static String mapToXml(Map<String, Object> map) {
        StringBuffer xml = new StringBuffer();
        xml.append("<xml>");
        for (String key : map.keySet()) {
            xml.append("<").append(key).append(">")
                    .append(map.get(key))//.append("<![CDATA[") .append("]]>")
                    .append("</").append(key).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }

}
