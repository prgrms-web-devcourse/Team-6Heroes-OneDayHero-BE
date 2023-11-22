package com.sixheroes.onedayheroapplication.main.converter;

public final class PointConverter {

    private PointConverter() {

    }

    public static String pointToString(double x, double y) {
        return y + " " + x;
    }
}
