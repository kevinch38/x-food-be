package com.enigma.x_food.util;

public class PagingUtil {
    public static Integer validatePage(Integer page) {
        return page <= 0 ? 1 : page;
    }

    public static Integer validateSize(Integer size) {
        return size <= 0 ? 10 : size;
    }

    public static String validateDirection(String direction) {
        return direction.equalsIgnoreCase("ASC") || direction.equalsIgnoreCase("DESC") ? direction : "ASC";
    }
}
