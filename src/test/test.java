import ZLYUtils.ExcelUtils;
import ZLYUtils.Network;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class test {
    public static void main(String[] args) throws IOException {
        String str;
        Map<Integer, Map<String, String>> mapMap = ExcelUtils.getExcelXlsx(new File("iwanvi_comment_comment.xlsx"));
        PrintWriter printWriter = new PrintWriter(new FileWriter("data.txt"),true);
        for(Iterator<Map.Entry<Integer, Map<String, String>>> iterator
            = mapMap.entrySet().iterator() ;iterator.hasNext();
            ){
            str = Network.getURLDecoderString(iterator.next().getValue().get("content"),"UTF-8");
            if(str.indexOf("�")!=-1){
                str=str.substring(0,str.indexOf("�"));
            }
            str = str.replace("%E","");
            str = str.replace("%","");
            printWriter.println(str);
        }
    }
}
