package com.collecdoo.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by toan.it
 * Date: 24/05/2016
 */
public class MyJsonObject<T> {
    @Expose
    public T data;
}
