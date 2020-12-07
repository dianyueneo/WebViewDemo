package com.wiseasy.weblib.utils;

import java.util.List;
import java.util.Map;

public class ParamsUtil {

    public static boolean isNotNull(Map map) {
        if (map != null && map.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
