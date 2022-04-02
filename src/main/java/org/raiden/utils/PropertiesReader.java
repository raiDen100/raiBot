package org.raiden.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesReader {
    public String getPropertyValue(String propertyName) {
        try{

            Properties properties = new Properties();
            InputStream ip= getClass().getResourceAsStream("/configs/" + System.getProperty("active.profile") + "/config.properties");//new FileInputStream("settings.properties"/*"src/main/resources/settings.properties"*/);

            Reader reader = new InputStreamReader(ip, StandardCharsets.UTF_8);

            properties.load(ip);
            return properties.getProperty(propertyName);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return "";
    }
}
