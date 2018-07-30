package ZLYUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveCrash {
    public static void save(String str){
        File file = new File("Crash.txt");
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(file,true));
            bf.write(WindosUtils.getDate()+":\n");
            bf.write((String)str);
            bf.write("\n");
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bf!=null){
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
