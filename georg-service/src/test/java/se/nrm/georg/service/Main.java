/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.georg.service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author idali
 */
public class Main {

  public static void main(String... args) {
    String jsonStr = "{\n"
            + "	\"features\": [],\n"
            + "	\"type\": \"FeatureCollection\"\n"
            + "}";
    JSONObject json = new JSONObject(jsonStr);
    System.out.println("json..." + json);
    JSONArray features = json.getJSONArray("features");
    System.out.println("features..." + features);
  }
  

}
