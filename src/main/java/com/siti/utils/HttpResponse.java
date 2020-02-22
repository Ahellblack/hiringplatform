package com.siti.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class HttpResponse {

    public static String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        try {
            URI uri = new URI(allConfigUrl);
            URL url = uri.toURL();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();
            StringBuilder result = new StringBuilder();
            // 读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
}
