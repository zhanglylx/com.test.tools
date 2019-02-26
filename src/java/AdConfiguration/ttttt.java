package AdConfiguration;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ttttt {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("t.txt"));
        String s;
        String str="";
        while ((s=bufferedReader.readLine())!=null){
            str+=s;
        }
        JSONObject jsonObject = JSONObject.fromObject(str);
        System.out.println(jsonObject);
        System.out.println(jsonObject.getString("hotBooks"));
        JSONArray jsonArray = jsonObject.getJSONArray("hotBooks");
        for(int i=0;i<jsonArray.size();i++){
            System.out.println(jsonArray.get(i));
            System.out.println(jsonArray.getJSONObject(i).getString("id"));
        }
    }
}
