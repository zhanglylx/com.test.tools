package ZLYUtils;

import javax.swing.JButton;
import java.net.URI;
import java.net.URISyntaxException;

public class test {
    JButton open=null;
    public static void main(String[] args) throws URISyntaxException {
        System.out.println(HttpUtils.getURI("http://cxtest1.ikanshu.cn/cx/itf/mySidebar?1+1","11=11"));
    }

}  