package SquirrelFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class test {
    public static void main(String [] args){
        Frame frame = new Frame("我的第一个窗口");
        frame.setSize(400,600);//设置窗体宽高
        frame.setLocationRelativeTo(null);//设置窗体位置,中间显示
//        frame.setLocation(500,200);//指定位置
        //设置图标
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage("V.png")
        );
        Button b1 = new Button("Button1");
        frame.add(b1);
        //设置布局管理器
        frame.setLayout(new FlowLayout());
        //设置退出监听器
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("鼠标单击组件");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("鼠标按下组件");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("鼠标释放");

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("鼠标进入组件");

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("鼠标离开组件");

            }
        });
        b1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("鼠标单击按钮");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("按钮按钮");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("鼠标释放");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("进入按钮");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("离开按钮");
            }

        });
        frame.setVisible(true);//设置窗口可见
    }
}
