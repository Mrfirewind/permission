package com.zhangwq.util;

import org.apache.commons.lang3.StringUtils;

public class LevelUtil {

    public static final String SEPARATOR = ".";

    public static final String ROOT = "0";

    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        }
        return StringUtils.join(parentLevel, SEPARATOR, parentId);
    }
}
