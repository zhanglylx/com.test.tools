package SquirrelFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Menubar extends JPanel {
    public static final String regards = "关于";
    public Menubar() {
        JMenuBar bar = new JMenuBar();
        bar.setBackground(Color.white);
        setLayout(new FlowLayout(0));
        JMenu jmenu = new JMenu("帮助");
        jmenu.setBorder(BorderFactory.createEtchedBorder());
        JMenuItem guanyu = new JMenuItem(regards);
        guanyu.setBackground(Color.white);
        guanyu.setBorder(BorderFactory.createEtchedBorder());
        jmenu.add(guanyu);
        bar.add(jmenu, BorderLayout.EAST);
        bar.setBorder(BorderFactory.createEtchedBorder());
        add(bar);
        buttonMouseListener(guanyu);
        setBackground(Color.white);
        setBorder(new EtchedBorder(EtchedBorder.RAISED));
    }
    public void buttonMouseListener(JMenuItem f) {
        String text = f.getText();
        f.addActionListener(e -> {
            new WindowsText(text,HomePage.getHomePage());
        });
    }
}
