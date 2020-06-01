package ZLYUtils;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class JTextAreadUtil {

    /**
     * 将文本域焦点设置到最后
     *
     * @param jTextAreaMoveEnd
     */
    public static void setJTextFocusFinal(JTextComponent jTextAreaMoveEnd) {
        //        //下面的代码就是移动到文本域的最后面
        jTextAreaMoveEnd.selectAll();
        if (jTextAreaMoveEnd.getSelectedText() != null) {
            jTextAreaMoveEnd.setCaretPosition(jTextAreaMoveEnd.getSelectedText().length());
            jTextAreaMoveEnd.requestFocus();
        }
    }

    public static void append(JTextArea jtext, boolean isLine, boolean isFocusFinal, String messageSeparator, Object... message) {
        if (jtext == null) throw new NullPointerException();
        if (message == null) return;
        if (messageSeparator == null) {
            jtext.append(StringUtils.join(message));
        } else {
            jtext.append(StringUtils.join(message, messageSeparator));
        }
        if (isLine) jtext.append("\n");
        if (isFocusFinal) setJTextFocusFinal(jtext);
    }

    public static void append(JTextArea jtext, Object... message) {
        append(jtext, message, "-");
    }

    public static void append(JTextArea jtext, Object[] message, String messageSeparator) {
        append(jtext, true, true, messageSeparator, message);
    }
}
