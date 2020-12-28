package ZLYUtils;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class WindosUtils {
    /**
     * 打开文件
     *
     * @param file
     * @throws IOException
     */
    public static void openFile(String file) {
        File fe = new File(file);
        String err = file;
        if (!fe.exists() && (file = getPuth(file)) == null) {
            TooltipUtil.errTooltip(err + "没有找到");
            return;
        }
        try {
            cmd("cmd /c explorer " + file);
            System.out.println(file);
        } catch (IOException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            TooltipUtil.errTooltip("打开" + file + "失败，请联系管理员");
        }
    }

    /***
     * 测试主机Host的port端口是否被使用
     * @param host
     * @param port
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress Address = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(Address, port);  //建立一个Socket连接
            flag = true;
        } catch (IOException e) {

        }
        return flag;
    }

    /**
     * 查询使用端口的PID
     *
     * @param netstat
     * @return PID
     */
    public static int selectNetstatPid(int netstat) {
        if (!String.valueOf(netstat).matches("\\d+")) throw new IllegalArgumentException(
                "参数错误，不是整数:" + netstat);
        try {
            String[] cmd = cmd("netstat -ano ");
            String result = null;
            for (int i = 0; i < cmd.length; i++) {
                if (cmd[i].contains("127.0.0.1:" + netstat + " ")) {
                    result = cmd[i];
                    break;
                }
            }
            if (result == null) return -1;
            result = result.substring(result.lastIndexOf(" "), result.length()).trim();
            return Integer.parseInt(result);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 复制文件
     *
     * @param jFrame
     * @param filePath
     * @param jbutton
     */
    public static void copyFile(JFrame jFrame, String filePath, JButton jbutton) {
        if (copyFile(jFrame, filePath)) {
            jbutton.setIcon(new ImageIcon(("image/succeed.png")));
        } else {
            jbutton.setIcon(new ImageIcon(("image/err.png")));
        }
    }

    /**
     * 复制文件
     *
     * @param jFrame
     * @param filePath
     */
    public static boolean copyFile(JFrame jFrame, String filePath) {
        File fe = new File(filePath);
        String err = filePath;
        if (!fe.exists() && (filePath = getPuth(filePath)) == null) {
            TooltipUtil.errTooltip(err + "没有找到");
            return false;
        }
        fe = new File(filePath);
        String copyPath = null;
        try {
            copyPath = SwingUtils.saveFileFrame(jFrame, new File(filePath));

        } catch (IllegalArgumentException e) {
            SaveCrash.save(e.toString());
        }
        if (copyPath == null) return false;
        //文件如果存在，重新名称
        if (new File(copyPath).exists()) {
            int filePathIndex = copyPath.lastIndexOf(".");
            StringBuffer sb = new StringBuffer(copyPath);
            sb.insert(filePathIndex, WindosUtils.getDate(" MM-dd-HH-mm-ss"));

            copyPath = sb.toString();
        }

        try (FileChannel fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ);
             FileChannel outfFileChannel = FileChannel.open(Paths.get(copyPath), StandardOpenOption.WRITE)) {
            zeroCopy(fileChannel, outfFileChannel);
            TooltipUtil.generalTooltip("保存成功:" + copyPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        InputStream ips = null;
//        OutputStream ops = null;
//        try {
//            ips = new FileInputStream(fe);
//            byte[] ipsBuffer = new byte[ips.available()];
//            ips.read(ipsBuffer);
//            ops = new FileOutputStream(copyPath);
//            ops.write(ipsBuffer);
//            ops.flush();
//            TooltipUtil.generalTooltip("保存成功:" + copyPath);
//            return true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (ops != null) {
//                try {
//                    ops.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (ips != null) {
//                try {
//                    ips.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return false;
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(File savePath, File copyPath) {

        if (savePath == null || copyPath == null)
            throw new IllegalArgumentException("复制或保存地址为空");
        if (!copyPath.exists()) TooltipUtil.errTooltip("没有找到复制文件地址");
        //文件如果存在，重新名称
        if (savePath.exists()) {
            int filePathIndex = savePath.getPath().lastIndexOf(".");
            StringBuffer sb = new StringBuffer(savePath.getPath());
            if (filePathIndex != -1) {
                sb.insert(filePathIndex, WindosUtils.getDate(" MM-dd-HH-mm-ss"));
            } else {
                sb.append(WindosUtils.getDate(" MM-dd-HH-mm-ss"));
            }
            savePath = new File(sb.toString());
        }
        InputStream ips = null;
        OutputStream ops = null;
        try {
            ips = new FileInputStream(copyPath);
            byte[] ipsBuffer = new byte[ips.available()];
            ips.read(ipsBuffer);
            ops = new FileOutputStream(savePath);
            ops.write(ipsBuffer);
            ops.flush();
            TooltipUtil.generalTooltip("保存成功:" + savePath);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ops != null) {
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ips != null) {
                try {
                    ips.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(String savePath, File copyPath) {
        if (savePath == null || copyPath == null)
            throw new IllegalArgumentException("复制或保存地址为空");
        if (!copyPath.exists()) TooltipUtil.errTooltip("没有找到复制文件地址");
        try (FileChannel inFileChannel = FileChannel.open(Paths.get(copyPath.getPath()), StandardOpenOption.READ);
             FileChannel outFileChannel = FileChannel.open(Paths.get(savePath), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.READ)) {
            zeroCopy(inFileChannel, outFileChannel);
            return true;
//            MappedByteBuffer 只有在GC回收时才会被释放资源,
//            MappedByteBuffer inMappedByteBuffer = inFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inFileChannel.size());
//            MappedByteBuffer outMappedByteBuffer = outFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, inFileChannel.size());
//            outMappedByteBuffer.put(inMappedByteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void zeroCopy(FileChannel fileChannel, WritableByteChannel target) throws IOException {
        long size = fileChannel.size();
        long position = 0;
        while (size > 0) {
            long tf = fileChannel.transferTo(position, size, target);
            if (tf > 0) {
                position += tf;
                size -= tf;
            }
        }
    }

    public static void main(String[] args) {
        copyFile("G:/MUSIC_copy.zip", new File("G:/music.zip"));
    }


    /**
     * 选中文件
     *
     * @param File
     * @throws IOException
     */
    public static void selectFile(String File) throws IOException {
        cmd("cmd /c explorer  /select, " + File);
    }

    /**
     * 执行windows系统dos命令
     *
     * @param code
     * @return
     */
    public static String[] cmd(String code) throws IOException {
        Process pro;
        BufferedReader br = null;
        String[] arr = new String[0];
        try {
            pro = Runtime.getRuntime().exec(code);
            br = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName("GBK")));
            String str = null;
            while ((str = br.readLine()) != null) {
                arr = Arrays.copyOf(arr, arr.length + 1);
                arr[arr.length - 1] = new String(str.getBytes(), "GBK");

            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return arr;
    }

    /**
     * 获取本机IP地址
     *
     * @return
     */
    public static String getLocalIP() {
        String ip = "";
        try {
            String[] str = cmd("ipconfig");
            String title = "";
            String index = "";
            for (String s : str) {
                if ((s.contains(":") && !s.contains(".")) || s.contains("适配器")) {
                    title = s;
                }
                if (s.contains("IPv4") || s.toUpperCase().contains("IPv4".toUpperCase())) {
                    if (!title.equals(index)) {
                        ip += title + "\n";
                        index = title;
                    }
                    ip += s + "\n";
                    ip += "\n";
                }

            }
        } catch (IOException e) {
            SaveCrash.save(e.toString());
        }
        return ip;
    }

    /**
     * 自动搜索文件地址
     *
     * @return
     */
    public static String getPuth(String fileName) {
        String msg = null;
        String name = fileName;
        String file = fileName.substring(fileName.lastIndexOf(File.separator) + 1
                , fileName.length());
        fileName = fileName.substring(0, fileName.lastIndexOf(File.separator) + 1);
        file = "*" + file + "*";
        fileName += file;
        try {
            Process pro = Runtime.getRuntime().exec("cmd /c dir/s/a/b " + fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName("GBK")));
            while ((msg = br.readLine()) != null) {
                if (msg.contains(name)) {
                    return msg;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定目录下的文件或目录
     *
     * @param path
     * @return
     */
    public static File[] getDirectoryFilesName(String path) {
        if (path == null) {
            TooltipUtil.errTooltip("getFilesName路径为空：" + path);
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            TooltipUtil.errTooltip("getFilesName文件不存在：" + path);
            return null;
        }
        if (!file.isDirectory()) {
            TooltipUtil.errTooltip("不是目录：" + path);
            return null;
        }
        return file.listFiles();

    }

    /**
     * 获取指定PID名称
     *
     * @return
     */
    public static String getPIDName(int pid) {
        if (!String.valueOf(pid).matches("\\d+")) throw new IllegalArgumentException("参数不合法:" + pid);
        try {
            String[] request = cmd("tasklist");
            for (int i = 0; i < request.length; i++) {
                String r = request[i];
                if (r.equals("")) continue;
                r = r.trim();
                r = r.substring(r.indexOf(" "), r.length()).trim();
                if (r.startsWith(String.valueOf(pid))) {
                    r = request[i].trim();
                    return r.substring(0, r.indexOf(" "));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取指定进程的pid
     *
     * @param process
     * @return -1为未获取
     */
    public static int[] getProcessPID(String process) {
        if (process == null) throw new IllegalArgumentException("process为空");
        String[] processArrays = new String[0];
        try {
            processArrays = cmd("tasklist");
        } catch (IOException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            TooltipUtil.errTooltip("获取指定进程PID失败" + process);
        }
        int[] getPID = new int[0];
        for (String s : processArrays) {
            if (s.startsWith(process)) {
                String str;
                try {
                    if (!s.contains("Console")) continue;
                    str = s.substring(0, s.indexOf("Console")).trim();
                    str = str.substring(str.lastIndexOf(" "), str.length()).trim();
                    if (str.matches("\\d+")) {
                        getPID = Arrays.copyOf(getPID, getPID.length + 1);
                        getPID[getPID.length - 1] = Integer.parseInt(str);
                    } else {
                        TooltipUtil.errTooltip("获取进行pid发生错误，请务必联系管理员，谢谢");
                        SaveCrash.save("获取进行pid发生错误，请联系管理员" + s);
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    SaveCrash.save("截取pid错误:" + s + "   " + e.toString());
                    return getPID;
                }

            }
        }
        return getPID;
    }

    public static List<String> dosExecute(String code) {
        return dosExecute(code, null, false, true, true);
    }

    public static List<String> dosExecute(String code, JTextArea jTextArea, boolean isWait, boolean isInputStream, boolean isErrorStream) {
        List<String> list = new ArrayList<>();
        Process pro = null;
        BufferedReader br = null;
        try {
            pro = Runtime.getRuntime().exec(code);
            if (isWait) pro.waitFor();
            if (isInputStream) {
                br = new BufferedReader(new InputStreamReader(pro.getInputStream(), StandardCharsets.UTF_8));
                list.addAll(getBufferedReader(br, jTextArea));
            }
            if (isErrorStream) {
                //获取错误流
                br = new BufferedReader(new InputStreamReader(pro.getErrorStream(), StandardCharsets.UTF_8));
                list.addAll(getBufferedReader(br, jTextArea));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (jTextArea != null) JTextAreadUtil.append(jTextArea, e);
        } finally {
            close(br, pro);
        }
        return list;
    }

    /**
     * 读取adb缓冲流
     *
     * @return
     */
    public static List<String> getBufferedReader(BufferedReader br) throws IOException {
        return getBufferedReader(br, null);
    }


    /**
     * 读取adb缓冲流
     *
     * @return
     */
    public static List<String> getBufferedReader(BufferedReader br, JTextArea jTextArea) throws IOException {
        String msg;
        List<String> list = new ArrayList<>();
        while ((msg = br.readLine()) != null) {
            list.add(msg);
            if (jTextArea != null) JTextAreadUtil.append(jTextArea, msg);
        }
        return list;
    }

    public static void close(BufferedReader bufferedReader, Process process) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (process != null) process.destroy();
    }

    /**
     * 关闭指定的进程
     *
     * @param process
     * @return
     */
    public static boolean closeProcess(String process) {
        if (process == null) throw new IllegalArgumentException("process为空");
        int[] closePID = getProcessPID(process);
        if (closePID.length == 0) return true;
        try {
            for (int pid : closePID) {
                if (Arrays.toString(
                        //taskkill /im 通过名称关闭  /f
                        cmd("taskkill /pid " + pid + "  /f")).contains("错误:")) return false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            TooltipUtil.errTooltip("关闭进程发生错误,进程名称:" + process);
            SaveCrash.save("关闭进程发生错误,进程名称:" + process + "     " + e.toString());
        }
        if (getProcessPID(process).length != 0) {
            System.out.println(Arrays.toString(getProcessPID(process)));
            return false;
        } else {
            return true;
        }


    }

    /**
     * 获取系统时间
     *
     * @param format 指定的格式
     * @return
     */
    public static String getDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取系统时间，默认时间格式:yyyy-MM-dd HH:mm:ss
     *
     * @return date
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd HH:mm:ss");
    }


}
