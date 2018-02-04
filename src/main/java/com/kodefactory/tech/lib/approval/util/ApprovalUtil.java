package com.kodefactory.tech.lib.approval.util;

public class ApprovalUtil {
    public static String getEntityName(Class klass){
        String[] nameSplit = klass.getName().replace(".", " ").split(" ");
        return nameSplit[nameSplit.length-1];
    }

    public static void main(String[] args){
        String name = "com.kodefactory.tech.security.domain.RoleEO";
        String[] split = name.replace(".", " ").split(" ");
        System.out.println(split.length);
        System.out.println(name.replace(".", " "));
    }
}
