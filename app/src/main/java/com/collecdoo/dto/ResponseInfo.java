package com.collecdoo.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by kiemhao on 6/21/16.
 */
public class ResponseInfo {
    public String status;
    public String message;
    public JsonElement data;

    public String getData() {
        return new Gson().toJson(data);
    }


}
