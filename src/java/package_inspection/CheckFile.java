package package_inspection;

import ZLYUtils.*;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 检查文件包大小
 */
public class CheckFile {
    private String[] files;//文件数组
    private String path;//文件目录
    private int filesNumber;//文件数量
    private Map<String, String> fileSizeCheckResult;//文件大小检验结果
    //用于统计apk中是否出现了文件大小不一致的次数，如果等于1，代表所有文件大小相同
    private int checkFileSizeCount;


    public CheckFile(String path) {
        this.path = path;
        this.files = SwingUtils.addFilesShiftArrays(
                WindosUtils.getDirectoryFilesName(path), new String[0]);
        this.fileSizeCheckResult = new LinkedHashMap<>();
        this.filesNumber = files.length;
        this.checkFileSizeCount = 0;
    }

    /**
     * 检查包大小
     */
    public void checkFileSize() {
        File file;
        String size;
        for (String fileName : files) {
            file = new File(path + File.separator + fileName);
            //判断文件是否存在
            if (!file.exists()) {
                this.fileSizeCheckResult.put("-1", fileName + " 文件不存在");
                this.checkFileSizeCount=2;
                //判断文件尾缀是否为apk包
            } else if (!fileName.endsWith(".apk")) {
                this.fileSizeCheckResult.put("-1", fileName + " 不是.apk包");
                this.checkFileSizeCount=3;
            } else {
                //获取文件大小KB，进行四舍五入
                size = String.valueOf(DoubleOperationUtils.div(file.length()*1.0,1024.0,1))+" KB";
                if (this.fileSizeCheckResult.containsKey(size)) {
                    this.fileSizeCheckResult.put(size,
                            this.fileSizeCheckResult.get(size) + "," + fileName);
                } else {
                    this.fileSizeCheckResult.put(size, fileName);
                    this.checkFileSizeCount++;
                }
            }

        }
    }

    public Map<String, String> getFileSizeCheckResult() {
        return fileSizeCheckResult;
    }


    public String[] getFiles() {
        return files;
    }


    public static void main(String[] args) {
        CheckFile checkFile = new CheckFile("./apk");
        checkFile.checkFileSize();
        for (Map.Entry<String, String> entry : checkFile.getFileSizeCheckResult().entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println(checkFile.getCheckFileSizeCount());
        System.out.println(Uiautomator.getElement());
    }

    public int getFilesNumber() {
        return filesNumber;
    }

    public String getPath() {
        return path;
    }

    public int getCheckFileSizeCount() {
        return checkFileSizeCount;
    }
}
