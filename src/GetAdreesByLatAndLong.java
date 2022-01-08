import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetAdreesByLatAndLong {

    /**
     * <p>
     * Title: GetLocationMsg
     * </p>
     * <p>
     * Description:高德地图逆地理编码
     * </p>
     *
     * @param longitude
     * @param latitude
     * @return 地址详情
     */
    public static String GetLocationMsg(double longitude, double latitude) {

        String message = "";

        String address = "";

        // 高德地图逆地理编码API
        String url = String.format(
                "https://restapi.amap.com/v3/geocode/regeo?output=JSON&key=高德的key&radius=1000&extensions=all&batch=false&roadlevel=0&location=%s,%s",
                longitude, latitude);

        URL myURL = null;

        URLConnection httpsConn = null;

        try {

            myURL = new URL(url);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        }

        try {

            httpsConn = (URLConnection) myURL.openConnection();
            httpsConn.setConnectTimeout(100000);
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;

                while ((data = br.readLine()) != null) {
                    message = message + data;
                }

                JsonParser jp = new JsonParser();
                //将收到的json字符串转化成json对象
                JsonObject jo = jp.parse(message).getAsJsonObject();

                String status = jo.get("status").getAsString();//返回状态

                String addressJsonEle = jo.get("regeocode").getAsJsonObject().get("formatted_address").toString();//结构化地址信息

                if (addressJsonEle.equals("[]")||status.equals("0")) {//返回不成功
                    address = null;

                } else {
//                    if (jo.get("regeocode").getAsJsonObject().get("pois").getAsJsonArray().size() <= 0) {//附近没有兴趣点
//                        String detail = jo.get("regeocode").getAsJsonObject().get("addressComponent").getAsJsonObject().get("streetNumber").getAsJsonObject().get("street").getAsString() + jo.get("regeocode").getAsJsonObject().get("addressComponent").getAsJsonObject().get("streetNumber").getAsJsonObject().get("number").getAsString();
//
//                        if (status.equals("1") && !addressJsonEle.equals("[]")) {
//                            address = addressJsonEle + " " + detail;
//
//                        }
//
//                    } else {//附近有兴趣点
//                        String detail = jo.get("regeocode").getAsJsonObject().get("pois").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
//
//                        String detailDistance = jo.get("regeocode").getAsJsonObject().get("pois").getAsJsonArray().get(0).getAsJsonObject().get("distance").getAsString();
//
//
//                        if (status.equals("1") && !addressJsonEle.equals("[]")) {
//                            address = addressJsonEle + " " + detail + " " + detailDistance.substring(0, detailDistance.lastIndexOf(".")) + "米";
//
//                        }
//                    }

                    address = addressJsonEle;

                }

                insr.close();

            }

        } catch (

                IOException e) {

            e.printStackTrace();

        }

        return address;

    }

    /**
     * <p>
     * Title: GetAddress
     * </p>
     * <p>
     * Description:高德地图地理编码
     * </p>
     *
     * @param address
     * @param city
     * @return 经纬度坐标
     */

    public static String GetAddress(String address, String city) {

        String result = "";

        String message = "";

        // 高德地图地理编码API

        String url = String.format(
                "https://restapi.amap.com/v3/geocode/geo?output=JSON&key=高德的key&address=%s&city=%s&batch=false",
                address, city);

        URL myURL = null;

        URLConnection httpsConn = null;

        try {

            myURL = new URL(url);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        }

        try {

            httpsConn = (URLConnection) myURL.openConnection();
            httpsConn.setConnectTimeout(100000);
            if (httpsConn != null) {

                InputStreamReader insr = new InputStreamReader(

                        httpsConn.getInputStream(), "UTF-8");

                BufferedReader br = new BufferedReader(insr);

                String data = null;

                while ((data = br.readLine()) != null) {

                    message = message + data;
                }

                JsonParser jp = new JsonParser();
                //将json字符串转化成json对象
                JsonObject jo = jp.parse(message).getAsJsonObject();

                String status = jo.get("status").getAsString();

                String addressJsonEle = jo.get("geocodes").getAsJsonArray().get(0).getAsJsonObject().get("formatted_address").toString();//结构化地址信息

                if (addressJsonEle.equals("[]")||status.equals("0")) {
                    result = null;

                } else {
                    String location = jo.get("geocodes").getAsJsonArray().get(0).getAsJsonObject().get("location").toString();
//                    result = addressJsonEle+" "+location;
                    result = location;
                }

                insr.close();

            }

        } catch (

                IOException e) {

            e.printStackTrace();

        }

        return result;

    }




    public final static void main(String[] args) {
        String gaodeResult = GetLocationMsg(116.343905925751, 39.9777942596322);
        System.out.println("高德地址===" + gaodeResult);
        String location = GetAddress(gaodeResult,"北京");
        System.out.println("高德坐标=="+location);

    }

}
