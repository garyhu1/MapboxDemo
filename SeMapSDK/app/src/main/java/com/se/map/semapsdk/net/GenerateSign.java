package com.se.map.semapsdk.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Author: Administrator;
 * Since 2018/4/16;
 * Description:
 */

public class GenerateSign {

    /**
     * KEY 及 SECRET
     */
    static String key = "99999999";// 更换成正式的KEY
    static String secret = "92D64124B7C53389D500083762E768E5";// 更换成正式的SECRET
    static String baseUrl = "http://lcs.emapgo.com.cn/api";
    static String search = "/poi/search";



//    public static void main(String[] args) {
//
//        // 定义参数
//        Map paras = new HashMap();
//        paras.put("lon", "121.39636500000006");
//        paras.put("sort", "0");
//        paras.put("area", "310000");
//        paras.put("range", "500");
//        paras.put("page", "1");
//        paras.put("type", "1");
//        paras.put("lat", "31.221566999999993");
//        paras.put("size", "10");
//
//        // 生成SIGN
//        String sign = getSign(paras);
//
//        // 生成请求的URL
//        String url = "?key=" + key;
//        Set keysSet = paras.keySet();
//
//        Iterator iterator = keysSet.iterator();
//        while (iterator.hasNext()) {
//            String k = (String) iterator.next();// key
//            Object v = paras.get(k);// value
//            url = url + "&" + k + "=" + v;
//        }
//
//        url = baseUrl + search + url + "&sign=" + sign;
//        System.out.println("url:" + url);
//
//        // 请求
//        String html = Request(url);
//        System.out.println("result:" + html);
//
//    }



    /**
     * 生成SIGN
     */
    public static String getSign(Map map) {

        String sign = "";
        String s = "";
        if (null != map) {

            // 重新生成请求参数顺序
            List list = sortMap(map);

            for (int i = 0; i < list.size(); i++) {
                s += list.get(i).toString() + map.get(list.get(i));
            }

        }

        try {
            sign = MD5(key + s + secret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }



    /**
     * MD5 加密
     *
     * @param strSrc
     * @return
     * @throws Exception
     */

    private static String MD5(String strSrc) throws Exception {

        byte[] data = strSrc.getBytes(Charset.forName("utf-8"));

        MessageDigest md5 = MessageDigest.getInstance("MD5");

        md5.update(data);

        byte b[] = md5.digest();

        int i;



        StringBuffer buf = new StringBuffer("");

        for (int offset = 0; offset < b.length; offset++) {

            i = b[offset];

            if (i < 0)

                i += 256;

            if (i < 16)

                buf.append("0");

            buf.append(Integer.toHexString(i));

        }



        return buf.toString().toUpperCase();// 转换成大写

    }



    /**

     * 对Map中key排序

     *

     * @param map

     * @return

     */

    private static List sortMap(Map map) {

        Set keyset = map.keySet();

        List list = new ArrayList(keyset);

        // 对key键值按字典升序排序

        Collections.sort(list);

        return list;

    }



    /**

     *

     */

    private static String Request(String urlString) {

        Map propertys = new HashMap();

        propertys

                .put("User-Agent",

                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; InfoPath.2)");

        propertys.put("Content-encoding", "gzip");

        propertys.put("Accept",

                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*");

        propertys.put("Connection", "keep-alive");

        propertys.put("Content-Type", "pplication/json;charset=utf-8");

        propertys.put("Accept-Language", "zh-CN,zh");

        propertys.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");



        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");

            if (propertys != null) {

                for (Object key : propertys.keySet()) {

                    urlConnection.addRequestProperty(key.toString(), propertys

                            .get(key).toString());

                }

            }

            String htmlString = "";



            InputStream inputStream = urlConnection.getInputStream();



            /**

             * 注意 目前API返回的数据做了数据压缩，所以需要用GZIP做解压；

             */

            GZIPInputStream gis = new GZIPInputStream(inputStream);



            InputStreamReader iReader = new InputStreamReader(gis, "UTF-8");



            BufferedReader bReader = new BufferedReader(iReader);



            String s = null;



            while ((s = bReader.readLine()) != null) {

                htmlString += s;

            }

            bReader.mark(0);

            bReader.reset();

            bReader.close();



            return htmlString;



        } catch (Exception e) {

            e.printStackTrace();

        }

        return "";

    }
}
