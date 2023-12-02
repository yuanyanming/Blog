package com.sangeng.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <V,O> List<V> copyBeanList(List<O> list, Class<V> clazz){
        Stream<O> stream = list.stream();
        Function<O,V> fun=e -> copyBean(e, clazz);
        Stream<V> vStream = stream.map(fun);
        List<V> collect = vStream.collect(Collectors.toList());
        return collect;
    }
}