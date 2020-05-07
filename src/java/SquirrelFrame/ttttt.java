package SquirrelFrame;

import ZLYUtils.HttpUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;

import java.util.Map;

public class ttttt {
    public static void main(String[] args) {
        int index = 0;
        while (true) {
            System.out.println(++index);
            String url = "http://web-cx-qa.ikanshu.cn/cx/book/recommend";
            Map<String, String> map = new HashMap<String, String>();
            map.put("bookId", "245786");
            map.put("cnid", "1062");
            map.put("type", "1");
            Map<String, String> hread = new HashMap<String, String>();
            hread.put("uid", "8");
            String response = HttpUtils.doPost(url, map, hread, null);
            JSONObject jsonObject = JSONObject.fromObject(response);
            JSONObject j1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = j1.getJSONArray("otherBookInfo");
            try {
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (!"null".equalsIgnoreCase(jsonArray.getJSONObject(i).getString("rank"))) {
                        System.out.println(response);
                        break;
                    }

                }
            } catch (Exception e) {
                System.out.println(response);
                break;
            }
        }

    }
}
