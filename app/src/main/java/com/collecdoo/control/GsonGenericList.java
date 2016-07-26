package com.collecdoo.control;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by kiemhao on 2/29/16.
 */
public class GsonGenericList<X> implements ParameterizedType {

    private Class<?> wrapped;

    public GsonGenericList(Class<X> wrapped) {
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[]{wrapped};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }

}