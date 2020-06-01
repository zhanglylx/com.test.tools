package ZLYUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;


public class AaptUtils {
    public static List<String> runAapt(String code) {
        return WindosUtils.dosExecute("platform-tools" + File.separator + "aapt.exe " + code);
    }

}
