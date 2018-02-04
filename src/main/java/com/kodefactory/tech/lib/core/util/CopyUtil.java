package com.kodefactory.tech.lib.core.util;

import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.exception.error.ApiError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Java Israel on 3/20/17.
 */
public class CopyUtil {

    /**
     * This utility method copies all field of an object into another object as long as the both have the same variable name.
     * @param source
     * @param destination
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void copy(Object source, Object destination) throws InvocationTargetException, IllegalAccessException {

        for(Method method : source.getClass().getDeclaredMethods()){
            String name = method.getName();
            if(name.startsWith("get")){
                Object sourceMethodValue = method.invoke(source);
                if(sourceMethodValue != null){
                    for(Method destMethod : destination.getClass().getDeclaredMethods()){
                        if(destMethod.getName().equalsIgnoreCase("set"+name.substring(3, name.length()))){
                            String sourceType = method.getReturnType().getName();
                            String destType = destMethod.getParameterTypes()[0].getName();
                            boolean equals = sourceType.equalsIgnoreCase(destType);
                            if(equals){
                                destMethod.invoke(destination, sourceMethodValue);
                            }
                        }
                    }
                }
            }
        }
    }


    public static void copy(Object source, Object destination, String[] fields) throws InvocationTargetException, IllegalAccessException {
        for(Method method : source.getClass().getDeclaredMethods()){
            String name = method.getName();
            if(name.startsWith("get")){
                Object sourceMethodValue = method.invoke(source);
                if(sourceMethodValue != null){
                    for(Method destMethod : destination.getClass().getDeclaredMethods()){
                        if(destMethod.getName().equalsIgnoreCase("set"+name.substring(3, name.length()))){
                            for(String field: fields){
                                if(field.equalsIgnoreCase(name.substring(3, name.length()))){
                                    String sourceType = method.getReturnType().getName();
                                    String destType = destMethod.getParameterTypes()[0].getName();
                                    boolean equals = sourceType.equalsIgnoreCase(destType);
                                    if(equals){
                                        destMethod.invoke(destination, sourceMethodValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static void copy(Map<String, Object> source, Object destination) throws InvocationTargetException, IllegalAccessException {
        /**
         * The use case of this method is to copy from a map into an object, as long as the map entry and object name is the same
         */
        for(Method destMethod : destination.getClass().getDeclaredMethods()){
            String name = destMethod.getName();
            if(destMethod.getName().equalsIgnoreCase("set"+name.substring(3, name.length()))){
                String field = name.substring(3, name.length());
                char prefix = field.toCharArray()[0];
                String fieldName = new String(prefix+"").toLowerCase()+field.substring(1, field.length());
                //System.out.println(fieldName);
                Object mapValue = source.get(fieldName);
                if(mapValue!=null){
                    String sourceType = mapValue.getClass().getName();
                    String destType = destMethod.getParameterTypes()[0].getName();
                    //System.out.println("Source Type: "+sourceType);
                    //System.out.println("Destination Type: "+destType);

                    if((destType.indexOf("Double")!=-1 || destType.indexOf("Float")!=-1 ) && (sourceType.indexOf("Integer")!=-1 || sourceType.indexOf("Byte")!=-1 || sourceType.indexOf("Long")!=-1 || sourceType.indexOf("Short")!=-1)){
                        Object castedValue = null;
                        if(destType.indexOf("Double")!=-1) castedValue = new Double(mapValue.toString());
                        if(destType.indexOf("Float")!=-1) castedValue = new Float(mapValue.toString());
                        destMethod.invoke(destination, castedValue);
                    }if((destType.indexOf("Integer")!=-1) && (sourceType.indexOf("Double")!=-1 || sourceType.indexOf("Float")!=-1 || sourceType.indexOf("Long")!=-1 || sourceType.indexOf("Short")!=-1)){
                        Object castedValue = new Integer(mapValue.toString());
                        destMethod.invoke(destination, castedValue);
                    }else{
                        System.out.println(destination);
                        System.out.println(mapValue);
                        destMethod.invoke(destination, mapValue);
                    }

                }
            }
        }
    }



    public static void copyRestObjects(Object source, Object destination) throws RestException {
        try {
            copy(source, destination);
        }catch (Exception ex) {
            throw new RestException(new ApiError(ex));
        }
    }

}
