package com.aspodev.utils;

import java.io.FileReader;
import java.io.IOException;
import com.aspodev.utils.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonReader {
    public static URL readUrl(String filePath) {
        JSONParser parser = new JSONParser();
        try(FileReader jsonfile = new FileReader(filePath)){
            JSONObject jsonObject = (JSONObject) parser.parse(jsonfile);
            String urlString = (String) jsonObject.get("url");
            URL url = new URL(urlString);
            return url;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Library readLibrary(String filePath, String library) {
        JSONParser parser = new JSONParser();
        try(FileReader jsonfile = new FileReader(filePath)){
            JSONObject jsonObject = (JSONObject) parser.parse(jsonfile);
            String labraryString = (String) jsonObject.get(library);
            if (labraryString != null) {
                String classs = (String) jsonObject.get("Class");
                String interfacee = (String) jsonObject.get("interface");
                String annotation = (String) jsonObject.get("annotation");
                String enume = (String) jsonObject.get("enum");
                Library libraryLib = new Library(library, classs, interfacee, annotation, enume);
                return libraryLib;
            }
            return null;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     *
     *
     *More methods to read Json files
     *
    */


    public static void main(String[] args) {
       URL url = readUrl(".\\jj.json");
        System.out.print(url.getUrl());
   }
}