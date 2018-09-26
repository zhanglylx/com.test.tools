package ZLYUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class JavaUtils {
    /**
     * 睡眠
     *
     * @param time
     */
    public static void sleep(Long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
        }
    }

    /**
     * 获取文件后缀名称
     *
     * @return
     */
    public static String getFileSuffixName(File file) throws IllegalArgumentException {
        if (file == null) throw new IllegalArgumentException("文件为空");
        if (!file.exists()) throw new IllegalArgumentException("文件不存在:" + file.getPath());
        if (file.getName().lastIndexOf(".") == -1) throw
                new IllegalArgumentException("不是一个文件:" + file.getName());
        return file.getName().substring(
                file.getName().lastIndexOf(".") + 1, file.getName().length());
    }


    public static void sleep(int time) {
        sleep((long) time);
    }

    public static int getRandomNumbers(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    /**
     * java方法
     */
    private void fangfa() {

        try {
            System.in.read();//按任意键
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //解析Html
    public void html() {
        String s = "<table id=kbtable><option value=\"0\">--选择广告--</option>," +
                "<option value=\"20180830175018\">直投错误图片</option></table>";
        String str;
        Document doc = Jsoup.parse(s);
        Elements elements = doc.select("option");
        Element element = elements.get(1);
        System.out.println(element.attributes().get("value"));
        System.out.println(element.text());
        int n;


    }

    /**
     * 解析html标签
     */
    public void parse() {
        String htmlStr = "<table id=kbtable >"
                + "<tr> "
                + "<td width=123>"
                + "<div id=12>这里是要获取的数据1</div>"
                + "<div id=13>这里是要获取的数据2</div>"
                + "</td>"
                + "<td width=123>"
                + "<div id=12>这里是要获取的数据3</div>"
                + "<div id=13>这里是要获取的数据4</div>"
                + "</td>	"
                + "</tr>"
                + "</table>";
        Document doc = Jsoup.parse(htmlStr);
        // 根据id获取table
        Element table = doc.getElementById("kbtable");
        // 使用选择器选择该table内所有的<tr> <tr/>
        Elements trs = table.select("tr");
        //遍历该表格内的所有的<tr> <tr/>
        for (int i = 0; i < trs.size(); ++i) {
            // 获取一个tr
            Element tr = trs.get(i);
            // 获取该行的所有td节点
            Elements tds = tr.select("td");
            // 选择某一个td节点
            for (int j = 0; j < tds.size(); ++j) {
                Element td = tds.get(j);
                // 获取td节点的所有div
                Elements divs = td.select("div");
                // 选择一个div
                for (int k = 0; k < divs.size(); k++) {
                    Element div = divs.get(k);
                    //获取文本信息
                    String text = div.text();
                    //输出到控制台
                    System.out.println(text);
                }
            }
        }
    }
}