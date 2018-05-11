package com.fenlibao.p2p.util.dm;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * jks 文件转换成 pfx
 * Created by zcai on 2016/8/26.
 */
public class PFX_JKS_Change {

    public static final String PKCS12 = "PKCS12";
    public static final String JKS = "JKS";
    public static String KEYSTORE_PASSWORD = "flb.123";
    public static String PFX_KEYSTORE_FILE = "C:\\Users\\Administrator\\Desktop\\huxing\\flb.ghb.app.dep.pfx";
    public static String JKS_KEYSTORE_FILE = "C:\\Users\\Administrator\\Desktop\\huxing\\flb.ghb.app.dep.jks";

    public static void converToPfx() {
        try {
            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(JKS_KEYSTORE_FILE);
            char[] nPassword = null;
            if ((KEYSTORE_PASSWORD == null) || "".equals(KEYSTORE_PASSWORD.trim())) {
                nPassword = null;
            } else {
                nPassword = KEYSTORE_PASSWORD.toCharArray();
            }
            try {
                inputKeyStore.load(fis, nPassword);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }
            fis.close();
            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");
            outputKeyStore.load(null, KEYSTORE_PASSWORD.toCharArray());
            Enumeration enums = inputKeyStore.aliases();
            while (enums.hasMoreElements()) {
                String keyAlias = (String) enums.nextElement();
                System.out.println("alias=["+ keyAlias +"]");
                if (inputKeyStore.isKeyEntry(keyAlias)) {
                    Key key = inputKeyStore.getKey(keyAlias, nPassword);
                    Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
                    outputKeyStore.setKeyEntry(keyAlias, key, KEYSTORE_PASSWORD.toCharArray(), certChain);
                }
            }
            FileOutputStream out = new FileOutputStream(PFX_KEYSTORE_FILE);
            outputKeyStore.store(out, nPassword);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        converToPfx();
    }

}
