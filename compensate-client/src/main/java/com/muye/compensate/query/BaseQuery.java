package com.muye.compensate.query;


import com.muye.compensate.exception.CompensateException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class BaseQuery extends Page implements Serializable {

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private String column;
    private String sort;


    public String getColumn() {

        if (column == null || column.length() == 0) {
            return column;
        }

        Field[] fields = this.getClass().getDeclaredFields();
        Field[] parentFields = this.getClass().getSuperclass().getDeclaredFields();

        column = column.toUpperCase();

        boolean b = Stream.concat(Stream.of(fields), Stream.of(parentFields))
                .map(d -> Stream.of(d.getName().split(""))
                            .map(s -> Character.isUpperCase(s.charAt(0)) ? "_" + s.toLowerCase() : s)
                            .reduce(String::concat)
                            .get())
                .anyMatch(f -> f.toUpperCase().equals(column));

        if (!b) {
            throw new CompensateException("排序字段必须为该表对应对象字段");
        }

        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getSort() {
        if (sort == null || sort.length() == 0) {
            return sort;
        }

        sort = sort.toUpperCase();

        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
