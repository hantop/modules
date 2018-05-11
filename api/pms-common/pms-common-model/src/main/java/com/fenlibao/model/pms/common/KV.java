package com.fenlibao.model.pms.common;

/**
 * 键值类
 *
 * Created by chenzhixuan on 2016/4/28.
 */
public class KV<K, V> {
    private K key;
    private V value;

    public KV() {
    }

    public KV(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "key:" + key + ", value:" + value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
