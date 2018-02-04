package com.kodefactory.tech.lib.core.util;

import com.kodefactory.tech.lib.exception.error.ApiError;
import com.kodefactory.tech.lib.exception.error.ApiSubError;
import com.kodefactory.tech.lib.exception.error.ApiValidationError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Java Israel
 */
public class ValidationUtil {

    public static boolean validateIsRequired(Object source, String... fields) {

        for(String field: fields) {
            for(Method method : source.getClass().getDeclaredMethods()) {
                if(method.getName().startsWith("get")){
                    String name = method.getName();
                    if (name.equals("get"+capitalizeFirstLetter(field))) {
                        try{
                            Object sourceMethodValue = method.invoke(source);
                            if(sourceMethodValue == null) {
                                return false;
                            }
                        }catch (Exception ex){
                            System.out.println(ex.getMessage());
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }


    public static ApiError buildValidateIsRequiredObject(Object source, String... fields) {
        if(validateIsRequired(source, fields)){
            return null;
        }
        ApiError apiError = new ApiError("Validation Error");
        List<ApiSubError> subErrorList = new ArrayList<>();

        for(String field: fields) {
            for(Method method : source.getClass().getDeclaredMethods()) {
                if(method.getName().startsWith("get")){
                    String name = method.getName();
                    if (name.equals("get"+capitalizeFirstLetter(field))) {
                        try{
                            Object sourceMethodValue = method.invoke(source);
                            if(sourceMethodValue == null) {
                                subErrorList.add(new ApiValidationError(source.getClass().getName(), field, field+" field is required", sourceMethodValue));
                            }
                        }catch (Exception ex){
                            subErrorList.add(new ApiValidationError(source.getClass().getName(), field, "Encountered an error validating this field"));
                        }
                    }
                }
            }
        }
        apiError.setSubErrors(subErrorList);
        return apiError;
    }



    private static String capitalizeFirstLetter(String word) {
        if(word == null) return "";
        String firstCapitalizedLetter = word.substring(0, 1).toUpperCase();
        return firstCapitalizedLetter+word.substring(1, word.length());
    }
}