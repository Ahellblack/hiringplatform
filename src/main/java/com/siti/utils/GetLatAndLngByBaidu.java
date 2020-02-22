package com.siti.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouyuhan on 2018-10-10.
 */
public class GetLatAndLngByBaidu {

    private static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            return "";
        }
        return json.toString();
    }

    public static Map<String, Double> getLngAndLat(String address, String AK) throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        String url = "http://api.map.baidu.com/geocoder/v2/?city=上海&address=" + address + "&output=json&ak=" + AK;
        String json = loadJSON(url);
        System.out.println(json);
        JSONObject obj = JSONObject.parseObject(json);
        if (obj != null) {
            if (obj.get("status").toString().equals("0")) {
                Double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                Double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                map.put("lng", lng);
                map.put("lat", lat);
                System.out.println("经度：" + lng + "---纬度：" + lat);
            } else {
                System.out.println("未找到相匹配的经纬度！");
                throw new Exception();
            }
        }
        return map;
    }

}
