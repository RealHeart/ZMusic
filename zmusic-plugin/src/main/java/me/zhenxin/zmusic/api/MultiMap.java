package me.zhenxin.zmusic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义一对多map
 */
public class MultiMap<K, V> {
    private final List<K> mkey;
    private final List<List<V>> mvlaue;

    public MultiMap() {
        mkey = new ArrayList<>();
        mvlaue = new ArrayList<>();

    }

    /*
     **添加元素
     */
    public void put(K key, V value) {
        List<V> list = new ArrayList<V>();
        list.add(value);
        if (containsKey(key)) {
            mvlaue.get(mkey.indexOf(key)).add(value);
        } else {
            mkey.add(key);
            mvlaue.add(list);
        }
    }

    /*
     *通过index获取key
     */
    public K getkey(int i) {
        return mkey.get(i);
    }

    /*
     *通过index获取values
     */
    public List<V> getvalue(int i) {
        return mvlaue.get(i);
    }

    /*
     *通过index获取元素
     */
    public Map<K, List<V>> get(int i) {
        Map<K, List<V>> map = new HashMap<>();
        map.put(mkey.get(i), mvlaue.get(i));
        return map;
    }

    /*
     *获取全部元素
     */
    public Map<K, List<V>> getAll() {
        Map<K, List<V>> map = new HashMap<>();
        for (int i = 0; i < mkey.size(); i++) {
            map.put(mkey.get(i), mvlaue.get(i));
        }
        return map;
    }

    //查看key是否重复
    public boolean containsKey(K key) {
        if (mkey.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     *大小
     */
    public long getSize() {
        return mkey.size();
    }

    /*
     *移除
     */
    public boolean removeAll() {
        mkey.clear();
        mvlaue.clear();
        if (mkey.isEmpty() && mvlaue.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}