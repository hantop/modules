package com.fenlibao.p2p.util.payment.tp.baofoo.loader;

import com.fenlibao.p2p.util.payment.tp.baofoo.BaoFooUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 */
public class CerLoader {

	private static String publicKeyRZZF;
	private static String publicKeyDF;

	public static String getPublicKeyRZZF() {
		return publicKeyRZZF;
	}
	public static String getWithdrawPublicKey() {
		return publicKeyDF;
	}

	static {
		publicKeyRZZF = getFileByteByPath(BaoFooUtil.CONFIG.rsaPubKeyPathRZZF());
		publicKeyDF = getFileByteByPath(BaoFooUtil.CONFIG.rsaPubPeyPathDF());
	}

	private static String getFileByteByPath(String path) {
		try (FileInputStream in = new FileInputStream(path)) {
			if (in == null) {
				throw new FileNotFoundException(path + " >> file not found");
			}
			byte[] reads = new byte[in.available()];
			in.read(reads);
			return new String(reads);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
