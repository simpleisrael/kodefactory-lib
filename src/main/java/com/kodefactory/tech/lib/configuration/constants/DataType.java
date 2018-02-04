package com.kodefactory.tech.lib.configuration.constants;

public enum DataType {
    INTEGER, STRING, LONG, DATE, DOUBLE, BOOLEAN;

    public static void main(String[] args) {
        System.out.println(DataType.valueOf("INTEGER"));
    }
}
