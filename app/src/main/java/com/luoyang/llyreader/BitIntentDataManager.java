package com.luoyang.llyreader;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局缓存
 *
 * @author luoyang
 * @date 2023/2/28
 */
public class BitIntentDataManager {
    public static Map<String, Object> bigData;

//    private static BitIntentDataManager instance = null;

    public static BitIntentDataManager getInstance() {
//        if (instance == null) {
//            synchronized (BitIntentDataManager.class) {
//                if (instance == null) {
//                    instance = new BitIntentDataManager();
//                }
//            }
//        }
        return Instance.INSTANCE;
    }

    private static class Instance {
        public static final BitIntentDataManager INSTANCE = new BitIntentDataManager();
    }

    private BitIntentDataManager() {
        bigData = new HashMap<>();
    }

    public Object getData(String key) {
        return bigData.get(key);
    }

    public void putData(String key, Object data) {
        bigData.put(key, data);
    }

    public void cleanData(String key) {
        bigData.remove(key);
    }
}
