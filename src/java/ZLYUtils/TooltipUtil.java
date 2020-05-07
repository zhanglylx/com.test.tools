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
    public static void setUIManager(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            ((Throwable) e).printStackTrace();
        }
    }



    /**
     * 选择框
     *
     * @param message
     * @param list
     * @return
     */
    public static String listSelectTooltip(String message, List<Object> list) {
        return listSelectTooltip(message,list.toArray());
    }
    public static String listSelectTooltip(String message, Object... list) {
        if (list.length < 1) {
            SaveCrash.save(new IllegalArgumentException("list为空").toString());
        }
        return String.valueOf(JOptionPane.showInputDialog(null,
                message + ":\n", "提示",
                JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), list, list[0]));
    }

    public static int yesNoTooltip(String text){
        setUIManager();
        return JOptionPane.showConfirmDialog(null, text, "松鼠",JOptionPane.YES_NO_OPTION); //返回值为0或1
    }

}
