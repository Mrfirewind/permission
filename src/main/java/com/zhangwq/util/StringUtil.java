package com.zhangwq.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {
    public static List<Integer> spliteToListInt(String str) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        List<Integer> list = strList.stream().map(strInt -> Integer.parseInt(strInt)).collect(Collectors.toList());
        return list;
    }
}
