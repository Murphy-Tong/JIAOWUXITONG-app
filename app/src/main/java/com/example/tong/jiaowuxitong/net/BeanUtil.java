package com.example.tong.jiaowuxitong.net;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by TONG on 2017/1/10.
 */
public class BeanUtil {


    public static Object trans(Object from, Class toClass) {
        if (from == null) return null;
        Field[] fs = from.getClass().getDeclaredFields();
        Method[] fms = from.getClass().getMethods();
        Object to = null;
        try {
            to = Class.forName(toClass.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < fms.length; i++) {
            Method method = fms[i];
//            String get = getterName(field.getName());
            if (method.getName().startsWith("get")) {
                try {
                    String setterName = setterName(method.getName());
                    Class type = method.getReturnType();
                    method.setAccessible(true);
                    Object w = method.invoke(from);
                    Method tm = toClass.getMethod(setterName, type);
                    tm.setAccessible(true);
                    tm.invoke(to, w);
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e) {
                } catch (IllegalAccessException e) {
                }
            }
        }
        return to;
    }

//
//    field.setAccessible(true);
//    Object w = field.get(from);
//    try {
//        String setter = setterName(field.getName());
//        Method toMethod = toClass.getDeclaredMethod(setter, field.getType());
//        toMethod.setAccessible(true);
//        toMethod.invoke(to, w);
//    } catch (NoSuchMethodException e) {
//    }

    private static String getterName(String field) {
        String ms = upper(field);
        return "get" + ms;
    }

    private static String setterName(String field) {
        String ms = upper(field.substring(3));
        return "set" + ms;
    }

    private static String upper(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}

