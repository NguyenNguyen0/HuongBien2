package huongbien.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JacksonUtil {
    public static <T> T readObjectFromFile(String filepath, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(new File(filepath), clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void writeObjectToFile(String filepath, T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            objectMapper.writeValue(new File(filepath), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Enable pretty printing with indentation
        objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        try {
            System.out.println(objectMapper.writeValueAsString(object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
