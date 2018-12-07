package performanceTest;

import ZLYUtils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 统计cpu，并将每次数据保存到根文件夹下
 */
public class GetIphoneData {
    private Statistical statistical;

    private BlockingDeque<Map<String, String>> blockingDeque;
    private Map<String, String> infoMap;
    private String packageName;
    public GetIphoneData(Statistical statistical) {
        this.statistical = statistical;
        this.blockingDeque = new LinkedBlockingDeque<>();
        this.infoMap = new LinkedHashMap<>();
        this.packageName="";
        new Thread(new SaveFile()).start();

    }

    public void run() {
        startGetIphoneData();
    }


    public void startGetIphoneData() {
        String[] p = AdbUtils.runAdb(
                "shell  top -n 1 ");
        int[] memory;
        double cpu;
        for (String s : p) {
            if (s.contains(this.statistical.getAppPackage())) {
                this.packageName = s.substring(s.lastIndexOf(" ")+1,s.length());
                cpu = getCpu(s);
                this.statistical.addCpu(cpu);
                memory = getMemory(s);
                this.statistical.addMemory(memory[0], memory[1]);
                this.infoMap.put(this.packageName+":cpu", String.valueOf(cpu));
                this.infoMap.put(this.packageName+":vss", String.valueOf(memory[0]));
                this.infoMap.put(this.packageName+":rss", String.valueOf(memory[1]));

            }
        }
        if(this.infoMap.size()>0) {
            this.infoMap.put("date", WindosUtils.getDate());
            this.blockingDeque.offer(this.infoMap);
        }
        this.infoMap=new LinkedHashMap<>();

    }

    /**
     * 解析字符串，获取CPU
     *
     * @param topLine
     * @return cpu
     */
    private double getCpu(String topLine) {
        int index = topLine.indexOf("%");
        if (index == -1) {
            throw new IllegalArgumentException("数据格式不匹配:" + topLine);
        }
        int start = topLine.lastIndexOf(" ", index);
        return Double.parseDouble(topLine.substring(start, index).trim());
    }


    /**
     * 解析内存
     *
     * @param topLine
     * @return int[0]:vss,int[1] rss
     */
    private int[] getMemory(String topLine) {
        int index = topLine.indexOf("K");
        int end = topLine.lastIndexOf("K");
        if (index == -1 || (index == end)) {
            throw new IllegalArgumentException("数据格式不匹配:" + topLine);
        }
        int start = topLine.lastIndexOf(" ", index);
        topLine = topLine.substring(start, end).replace("K", "");
        String[] str = new String[0];
        for (String s : topLine.split(" ")) {
            if (s.matches("\\d+")) {
                str = Arrays.copyOf(str, str.length + 1);
                str[str.length - 1] = s;
            }
        }

        if (str.length != 2) throw new IllegalArgumentException("数据格式不匹配:" + topLine);
        int[] d = new int[2];
        //VSS
        d[0] = Integer.parseInt(str[0]);
        //RSS
        d[1] = Integer.parseInt(str[1]);
        return d;
    }

    /**
     * 保存每一次获取cpu和性能的数据
     */
    class SaveFile extends Thread {
        private Map<Integer, Map<String, String>> mapMap;
        private File infoSaveFile;

        public SaveFile() {
            this.mapMap = new LinkedHashMap<>();
            this.infoSaveFile = new File(
                    ZLYUtils.WindosUtils.getDate("yyyy-MM-dd-HH-mm-ss") + ".xlsx");
        }

        public void run() {
            while (true) {
                if (blockingDeque.size() > 0) {
                    if (this.infoSaveFile.exists()) {
                        try {
                            this.mapMap = ExcelUtils.getExcelXlsx(this.infoSaveFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            TooltipUtil.errTooltip("获取Excel失败");
                            throw new IllegalArgumentException("获取Excel失败:" + this.infoSaveFile);
                        }
                        this.mapMap.put(this.mapMap.size(), blockingDeque.poll());
                        if (!this.infoSaveFile.delete()) {
                            TooltipUtil.errTooltip("删除文件失败");
                            throw new IllegalArgumentException("删除文件失败:" + this.infoSaveFile);
                        }
                        saveFile();
                    } else {
                        this.mapMap.put(0, blockingDeque.poll());
                        saveFile();
                    }
                }
            }
        }

        public void saveFile() {
            if (!ExcelUtils.createExcelFile(
                    this.infoSaveFile, "info", this.mapMap,false)) {
                TooltipUtil.errTooltip("保存文件失败");
                throw new IllegalArgumentException("保存文件失败:" + this.infoSaveFile);
            }
        }
    }
}
