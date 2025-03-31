package com.example.demo.Address;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

@Service
public class DouyinRoomInfo {
    // 需要添加 Gson 依赖：implementation 'com.google.code.gson:gson:2.8.9'

    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36";

 public  static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入抖音房间ID：");
        String roomId = scanner.nextLine();
        String nickname = tryMethod1(roomId);
        if (nickname == null) {
            nickname = tryMethod2(roomId);
        }
        if (nickname != null) {
            System.out.println("房间ID: " + roomId + " 的主播昵称是: " + nickname);
        } else {
            System.out.println("无法获取房间ID: " + roomId + " 的主播昵称。");
        }
 }

    public static String tryMethod1(String roomId) {
        try {
            String url = "https://webcast-open.douyin.com/webcast/openapi/room/reflow/info/?" +
                    "verifyFp=verify_m85c4gax_sVIyEE9W_1Tkd_4hjH_A9Av_V5M0Jv7H2jWs" +
                    "&live_id=1" +
                    "&room_id=" + roomId +
                    "&webcast_app_id=247160" +
                    "&msToken=" +
                    "&X-Bogus=DFSzKwVOo4vANjArt4ftMUkX95uo";

            String response = sendGetRequest(url);
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            String nickname = json.getAsJsonObject("data")
                    .getAsJsonObject("room")
                    .getAsJsonObject("owner")
                    .get("nickname").getAsString();
            System.out.println("方法一获取成功: " + nickname);
            return nickname;
        } catch (Exception e) {
            return null;
        }
    }

    public static String tryMethod2(String roomId) {
        try {
            String url = "https://webcast-open.douyin.com/webcast/openapi/room/reflow/info/?" +
                    "verifyFp=verify_m85c4gax_sVIyEE9W_1Tkd_4hjH_A9Av_V5M0Jv7H2jWs" +
                    "&live_id=1" +
                    "&room_id=" + roomId +
                    "&webcast_app_id=247160" +
                    "&msToken=MiOTmijuqLmBHsLqA7TuEDCUDrDcmWlf3occjY7Hr-xdwEbfYwVI_jClC5lIHVECj6X_jtrbHBgR-gcMZ1IghWQYRzXCzYYhmBPF7YAFitjDYl6h01YPVKjd4EMlTQ==" +
                    "&X-Bogus=DFSzKwVOr/UANjArt4fGCtkX95ur";

            String response = sendGetRequest(url);
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            String nickname = json.getAsJsonObject("data")
                    .getAsJsonObject("room")
                    .getAsJsonObject("owner")
                    .get("nickname").getAsString();
            return nickname;
        } catch (Exception e) {
            System.out.println("方法二未识别到");
            return null;
        }
    }

    public static String sendGetRequest(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        throw new RuntimeException("HTTP request failed with code: " + responseCode);
    }
}
