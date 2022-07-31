package com.bookingtest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigReader {

    public static Properties prop;

    public static String getProperty(String propertyName) {
        try (FileReader propertyFileReader = new FileReader("src/test/resources/config.properties")) {
            prop = new Properties();
            prop.load(propertyFileReader);

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return prop.get(propertyName).toString();
    }

    public static Object[][] getData(String fileName,String dataType) {

        Gson gson = new Gson();
        JsonArray ja = null;
        try {
            ja = gson.fromJson(
                    new FileReader("src/test/Resources/TestData/" + fileName + ".json"), JsonObject.class).get(dataType).getAsJsonArray();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        int row = ja.size();
        int col = ja.get(0).getAsJsonObject().size();
        Object[][] objarray = new Object[row][col];
        for (int i = 0; i < row; i++) {
            int j = 0;
            Set<Map.Entry<String, JsonElement>> es = ja.get(i).getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> e : es) {
                objarray[i][j] = e.getValue().toString().replace("\"", "");
                j++;
            }
        }
        return objarray;
    }
}
