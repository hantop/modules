package com.fenlibao.p2p.common.util.xstream;

import com.fenlibao.p2p.common.util.xstream.annotation.XStreamCDATA;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Writer;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2015/8/28.
 */
public class XStreamUtil {

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
        });
        xStream.autodetectAnnotations(true);
    }

    public static XStream newXStream(Class... classTypes) {
        if (classTypes != null) {
            for (Class classType : classTypes) {
                xStream.processAnnotations(classType);
            }
        }
        return xStream;
    }
}
