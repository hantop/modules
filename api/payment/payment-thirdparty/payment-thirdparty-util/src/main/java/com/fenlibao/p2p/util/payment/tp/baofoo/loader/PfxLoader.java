package com.fenlibao.p2p.util.payment.tp.baofoo.loader;

import com.fenlibao.p2p.util.payment.tp.baofoo.BaoFooUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 */
public class PfxLoader {

    private static byte[] privateKeyRZZF;
    private static byte[] privateKeyDF;

    public static byte[] getPrivateKeyRZZF() {
        return privateKeyRZZF;
    }
    public static byte[] getPrivateKeyDF() {
        return privateKeyDF;
    }

    static {
        privateKeyRZZF = getFileByteByPath(BaoFooUtil.CONFIG.rsaPriPeyPathRZZF());
        privateKeyDF = getFileByteByPath(BaoFooUtil.CONFIG.rsaPriPeyPathDF());
    }

    private static byte[] getFileByteByPath(String path) {
        try (FileInputStream in = new FileInputStream(path)) {
            if (in == null) {
                throw new FileNotFoundException(path + " >> file not found");
            }
            byte[] reads = new byte[in.available()];
            in.read(reads);
            return reads;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
