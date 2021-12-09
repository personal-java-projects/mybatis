package com.example.utils;

import java.util.*;

public class MultiplyType<K, Y> {
    private K k;
    private Y y;

    private static List classes;

    public MultiplyType(K k, Y y) {
        this.k = k;
        this.y = y;
    }


    public static <T> List<Map> getClassList(T...args) throws ClassNotFoundException {
        List<Map> classList = new ArrayList<Map>();

        for (T item : args){
            Map<String, T> map = new HashMap<String, T>();

            Class<T> className = (Class<T>) Class.forName(item.getClass().getName());
            String simpleName = item.getClass().getSimpleName();
            map.put(simpleName, (T) className);
            classList.add(map);
        }

        MultiplyType.setClasses(classList);

        return classList;
    }

    // 根据类获取指定对象
    public static <T> T getT(String simpleName) {
//        System.out.println("class: " + classes + "class<?>: " + simpleName);
        for (int i=0; i<classes.size(); i++) {

            HashMap hashMap = (HashMap) classes.get(i);
//            System.out.println(hashMap instanceof HashMap);

//            System.out.println("hashMap: " + hashMap + "String: " + hashMap.get(simpleName) instanceof String);

//            System.out.println("1: " + hashMap.entrySet());
//            System.out.println("2: " + simpleName);
//            if (hashMap.get(simpleName) == simpleName) {
////                System.out.println(hashMap.get(simpleName));
//
//            }
            return (T) hashMap.get(simpleName);
        }
        return null;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

    public static List getClasses() {
        return classes;
    }

    public static void setClasses(List classes) {
        MultiplyType.classes = classes;
    }

    @Override
    public String toString() {
        return "MultiplyType{" +
                "k=" + k +
                ", y=" + y +
                '}';
    }
}
