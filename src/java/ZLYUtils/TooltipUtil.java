package ZLYUtils;

import javax.swing.*;

public class TooltipUtil {
    /**
     * 错误提示框
     * @param errText
     */
    public static void errTooltip(String errText){
        JOptionPane.showMessageDialog(null, errText, "错误提示", JOptionPane.ERROR_MESSAGE);
    }
    /**
     *普通提示框
     */
    public static void generalTooltip(String generalText){
        JOptionPane.showMessageDialog(null, generalText);
    }

    /**
     * 选择框
     * @param message
     * @param list
     * @return
     */
    public static String listSelectTooltip(String message,String[] list){
        if(list.length<1){
            SaveCrash.save( new IllegalArgumentException("list为空").toString());
        }
       return  (String) JOptionPane.showInputDialog(null,
               message+":\n", "提示",
               JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), list,list[0]);
    }

    public static int yesNoTooltip(String text){
        return JOptionPane.showConfirmDialog(null, text, "松鼠",JOptionPane.YES_NO_OPTION); //返回值为0或1
    }

}
