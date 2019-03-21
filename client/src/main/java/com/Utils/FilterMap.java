package com.Utils;

import java.util.HashMap;

public class FilterMap extends HashMap {

    @Override
    public Object put(Object key, Object value) {
        Object str = "";
        if (this.containsKey(key)) {
            str = this.get(key);
            str = str + "\n" + value;
        } else {
            str = value;
        }
        this.put(key,str);

        return this;
    }




}