package com.fenlibao.p2p.weixin.xstream;

import com.fenlibao.p2p.weixin.message.Message;
import com.fenlibao.p2p.weixin.xstream.annotation.XStreamCDATA;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2015/8/28.
 */
public final class XStreamUtil {

    private static XStream xStream;

    static {
        xStream = new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    boolean cdata = false;
                    Class<?> targetClass = null;

                    @Override
                    public void startNode(String name, Class clazz) {
                        super.startNode(name);
                        if (!name.equals("xml")) {
                            cdata = needCDATA(targetClass, name);
                        } else {
                            targetClass = clazz;
                        }
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[" + text + "]]>");
                        } else {
                            writer.write(text);
                        }
                    }

                    private boolean needCDATA(Class<?> targetClass, String fieldAlias) {
                        boolean cdata = false;
                        cdata = existsCDATA(targetClass, fieldAlias);
                        if (cdata) return cdata;
                        Class<?> superClass = targetClass.getSuperclass();
                        while (!superClass.equals(Object.class)) {
                            cdata = existsCDATA(superClass, fieldAlias);
                            if (cdata) return cdata;
                            superClass = superClass.getClass().getSuperclass();
                        }
                        return false;
                    }

                    private boolean existsCDATA(Class<?> clazz, String fieldAlias) {
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.getAnnotation(XStreamCDATA.class) != null) {
                                XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
                                if (null != xStreamAlias) {
                                    if (fieldAlias.equals(xStreamAlias.value()))
                                        return true;
                                } else {
                                    if (fieldAlias.equals(field.getName()))
                                        return true;
                                }
                            }
                        }
                        return false;
                    }
                };
            }
        }){
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(Message.class);
    }

    public static Message fromXML(String xml) {
        return (Message) xStream.fromXML(xml);
    }

    public static Serializable toXML(Message result) {
        if (result == null) return "";
        return xStream.toXML(result);
    }
}
