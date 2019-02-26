package ZLYUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class tttttt {
    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, Map<String, String>> mapMap = ExcelUtils.getExcelXlsx(new File("单天曝光和点击.xlsx"));
        Map<Integer, Map<String, String>> mapMap2 = new LinkedHashMap<>();
        Map<String, String> mapMap1;
        Map.Entry<Integer, Map<String, String>> mapEntry;
        int index;
        String key = "enpected result";
        for (Iterator<Map.Entry<Integer, Map<String, String>>> iterator =
             mapMap.entrySet().iterator(); iterator.hasNext(); ) {
            mapEntry = iterator.next();
            mapMap1 = mapEntry.getValue();
            if (mapMap1.get(key).length() > 30) {
                mapMap1.put(key,
                        mapMap1.get(key).replace(
                                "{}", "").replace("\n","")+"\n"
                );

                System.out.println("===");
                System.out.println(mapMap1.get(key));
            }
            mapMap2.put(mapEntry.getKey(), mapMap1);
        }
        ExcelUtils.createExcelFile(new File("单天曝光和点击1.xlsx"), "ad", mapMap2);
    }
}
