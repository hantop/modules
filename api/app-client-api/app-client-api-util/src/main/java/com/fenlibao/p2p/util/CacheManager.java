package com.fenlibao.p2p.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存管理
 * 
 * @author chenzhixuan
 *
 */
public class CacheManager {
	private static Map<String, Object> cache = new HashMap<String, Object>();

	private CacheManager() {
	}

	/**
	 * 添加缓存
	 * 
	 * @param key
	 * @param value
	 */
	public static void putCache(String key, Object value) {
		if (cache != null) {
			cache.put(key, value);
		}
	}

	/**
	 * 根据key获取缓存
	 * 
	 * @param key
	 * @return
	 */
	public static <T> T getCache(String key) {
		T value = null;
		if (cache != null && cache.containsKey(key)) {
			value = (T) cache.get(key);
		}

		return value;
	}

	/**
	 * 防止不知value什么类型出现报错
	 * 
	 * @param key
	 * @return
	 */
	public static Object getCacheObject(String key) {
		Object result = null;
		if (cache != null && cache.containsKey(key)) {
			result = cache.get(key);
		}

		return result;
	}

	/**
	 * 获取所有缓存
	 * 
	 * @return
	 */
	public static Map<String, Object> getAllCache() {
		return cache;
	}
}
