package com.siti.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by quyue1205 on 2019/1/22.
 * 获取高德地图经纬度
 */
public class GetLatAndLngByGaode {
    private static Logger logger = LoggerFactory.getLogger(GetLatAndLngByGaode.class);


    private static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            return "";
        }
        return json.toString();
    }

    public static Map<String, Double> getLngAndLat(String address, String key) throws Exception, IOException {
        Map<String, Double> map = new HashMap<String, Double>();
        //String url = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=xml&key=" + key;  //xml格式
        String url = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=json&key=" + key;  //json格式
        logger.error("URL：{}", url);
        String json = loadJSON(url);
        System.out.println(json);
        //JSONObject obj = XmlTool.documentToJSONObject(json);
        JSONObject obj = JSONObject.parseObject(json);
        if (obj != null) {
            //JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(obj.toJSONString());
            JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(json);
            if ("1".equals(obj.get("status").toString()) && !"0".equals(obj.get("count").toString())) {
                JsonNode listSource = jsonNode.findValue("location");
                Double lng = Double.parseDouble(listSource.textValue().split(",")[0]);
                Double lat = Double.parseDouble(listSource.textValue().split(",")[1]);
                map.put("lng", lng);
                map.put("lat", lat);
                logger.info("获取高德地图经纬度 : 地址：{}；key：{}；经度-纬度：{}", address, key, listSource.textValue());
            } else {
                logger.error("获取高德地图经纬度 : 地址：{}；key：{}；未找到相匹配的经纬度！", address, key);
                throw new Exception("未找到相匹配的经纬度");
            }
        }
        return map;
    }

    //test
    /*public static void main(String[] args) {
        try {
            GetLatAndLngByGaode.getLngAndLat("上海市上海市浦东新区崂山路316-1号平安科技深圳有限公司", "b413b3183893f11ebc39ed32450112ae");
        } catch (MyException me) {
            me.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (DocumentException de) {
            de.printStackTrace();
        }
    }*/

}
