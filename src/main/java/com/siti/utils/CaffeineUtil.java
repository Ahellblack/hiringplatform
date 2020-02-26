package com.siti.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.siti.config.CaffeineInit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

/**
 * Created by dell on 2020/1/2.
 */
@Service
public class CaffeineUtil<K,V,T,R> {

    @Autowired
    private static Cache cache;

    private static com.siti.utils.CaffeineUtil CaffeineUtil;
    private CaffeineUtil(){
    }
    static {
        cache = CaffeineInit.getCaffeineConfig();
    }
    public static com.siti.utils.CaffeineUtil build() {
        return new CaffeineUtil();
    }


    public R getValues(String key,Function<T,R> function) {


        if (StringUtils.isBlank(key) ) {
            return null;
        }
        R res = (R)cache.getIfPresent(key);
        /*Iterable iterable = new Iterable() {
            @Override
            public Iterator iterator() {
                return null;
            }
        };
        Map map = cache.getAllPresent(iterable);*/
        if (Objects.isNull(res)) {//从db取值
            //obj = db.getdata;
            res = function.apply(null);
            cache.put(key, res);
        }
        return res;
    }

    public void put(String key,Object obj) {
        if (!StringUtils.isBlank(key) ) {
            cache.put(obj,key);
        }
    }

    public Object get(String key) {
        Object obj  = null;
        if (!StringUtils.isBlank(key) ) {
            obj = cache.getIfPresent(key);
        }
        return obj;
    }
}
