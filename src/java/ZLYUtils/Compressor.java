package ZLYUtils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * CND压缩机
 */
public class Compressor {
    private Charset _encoding = Charset.forName("GBK");

    public byte[] encode(String text) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (DeflaterOutputStream zip = new DeflaterOutputStream(out)) {
                byte[] bin = text.getBytes(_encoding);
                zip.write(bin);
            }
            byte[] ret = out.toByteArray();
            return ret;
        }
    }

    public String decode(byte[] bytes) throws IOException {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
                try (InflaterInputStream unzip = new InflaterInputStream(in)) {
                    byte[] buffer = new byte[256];
                    int n;
                    while ((n = unzip.read(buffer)) >= 0) {
                        out.write(buffer, 0, n);
                    }
                    return new String(out.toByteArray(), _encoding);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        String s = getStr(new File("3790601-1.dat"),"103035","3790601","1");
//        System.out.println(s);
//        s = getStr(new File("2070-1.dat"),"123383","2070","1");
//        System.out.println(s);
//        s = getStr(new File("5130656-1.dat"),"104269","5130656","1");
//        System.out.println(s);
//        s = getStr(new File("2339177-1.dat"),"121963","2339177","1");
//        System.out.println(s);
//        String s = getStr(new File("13149-0.dat"), "251740", "13149", "0");
//        System.out.println(s);
//        String s = getStr(new File("67367767-0.dat"), "808500526", "67367767", "0");
//        System.out.println(s);
//        Encryptor encryptor = new Encryptor("3u5k41jj94mijuef3b8ljbo19o".getBytes());
//        BufferedReader bufferedReader = new BufferedReader(new FileReader("ss.txt"));
//        String m;
//        String str = "";
//        while ((m = bufferedReader.readLine()) != null) {
//            str += m;
//        }
//        System.out.println(str);
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("gg.dat"));
//        byte[] bytes = encryptor.encode(str);
//        for(byte b : bytes){
//            System.out.println(b);
//            bufferedWriter.write(b);
//        }
//        bufferedWriter.flush();
//        bufferedReader.close();
        String s = getStr(new File("128-3.dat"), "272728", "128", "3");
        System.out.println(s);


    }


    public static String getStr(File file, String... key) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : key) {
            stringBuilder.append(s);
            stringBuilder.append("-");
        }
        stringBuilder.append("3u5k41jj94mijuef3b8ljbo19o");
        return new Encryptor(DigestUtils.md5(
                stringBuilder.toString())).decode(IOUtils.loadBytes(file));
    }
}

