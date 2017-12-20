package com.zhangwq.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class JsonData {

    private boolean result;
    private String msg;
    private Object data;

    public JsonData(boolean result) {
        this.result = result;
    }

    public static JsonData createBySuccess(Object data, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = data;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData createBySuccess(Object data) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = data;
        return jsonData;
    }

    public static JsonData createBySuccess() {
        return new JsonData(true);
    }

    public static JsonData createByError(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", this.result);
        result.put("msg", this.msg);
        result.put("data", this.data);
        return result;
    }

}
