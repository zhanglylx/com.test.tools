package performanceTest;

import ZLYUtils.DoubleOperation;
import ZLYUtils.ExcelUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

public class avg {
    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer,Map<String,String>> mapMap = ExcelUtils.getExcelXlsx(new File("掌阅退出后台第二次.xlsx"));
        double sum=0.0;
        double max = 0.0;
        double d=0.0;

        for(Map.Entry<Integer,Map<String,String>> entry:mapMap.entrySet()){
            d=Double.parseDouble(entry.getValue().get("com.chaozh.iReaderFree:vss"));
            if(d>max)max=d;
            sum=DoubleOperation.add(sum,d);
        }
        System.out.println("最大值:"+max);
        System.out.println("平均值:"+DoubleOperation.div(sum,mapMap.size(),2));
    }
}
