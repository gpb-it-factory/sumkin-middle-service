package com.gpb.sumkin_middle_service.logging;

import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

/** Сопоставляет имена параметров с их значениями и выводи строку с перечислением параметров*/
public class ParametersMapper {

    private final Map<String, String> argumentsMap;
    private final String[] parameterNames;
    private final Object[] parameterValues;

    public ParametersMapper(String[] parameterNames, Object[] parameterValues) {
        this.parameterNames = parameterNames;
        this.parameterValues = parameterValues;
        this.argumentsMap = new LinkedHashMap<>();
    }

    public String parse() {

        for (int i=0; i<parameterNames.length; i++) {
            String key = parameterNames[i];
            String value = getParamsString(parameterValues[i]);
            this.argumentsMap.put(key, value);
        }
        return parametersToString();
    }

    private String parametersToString() {
        return this.argumentsMap.toString();
    }

    //TODO попробовать сделать вывод параметров в строку с помощью рефлексии
    private String getParamsString(Object object) {

        String result;
        if (object instanceof MultipartFile file) {
            result = "{name=" + file.getOriginalFilename() + ", size=" + file.getSize() +
                     ", content=" + file.getContentType() + "}";
        } else {
            result = object.toString();
        }
        return result;
    }
}
