package performanceTest;

import ZLYUtils.DoubleOperationUtils;
import ZLYUtils.ExcelUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * 计算平均值
 */
public class avg {
    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer,Map<String,String>> mapMap = ExcelUtils.getExcelXlsx(new File("2020-01-07-16-33-05.xlsx"));
        double sum=0.0;
        double max = 0.0;
        double d=0.0;

        for(Map.Entry<Integer,Map<String,String>> entry:mapMap.entrySet()){
            d=Double.parseDouble(entry.getValue().get("com.mfyueduqi.book:cpu"));
            if(d>max)max=d;
            sum= DoubleOperationUtils.add(sum,d);
        }
        System.out.println("最大值:"+max);
        System.out.println("平均值:"+ DoubleOperationUtils.div(sum,mapMap.size(),2));
    }
//    cpu  最大：16   平均：9.63
//    cpu  最大：6   平均：0.05

//    com.mfyueduqi.book:vss： 最大：2095396   平均：2071989
//    com.mfyueduqi.book:vss： 最大：2054472   平均：2050870.83

//    com.mfyueduqi.book:rss： 最大：173204    平均：165887.04
//    com.mfyueduqi.book:rss： 最大：162532    平均：158745.49

//
}
