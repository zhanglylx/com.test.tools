package UI;

import javax.swing.*;
import java.awt.*;

public class 下拉列表与滚动条  extends JFrame {
    JPanel panel1,panel2;
    JLabel label1,label2;
    JComboBox box;//设置下拉列表
    JList list;//
    JScrollPane sp;//设置滚动条

    下拉列表与滚动条(){
        panel1=new JPanel();
        panel2=new JPanel();
        label1=new JLabel("籍贯");
        label2=new JLabel("学历");
        String [] address= {"北京","天津","上海","福建","广州"};//用个数组
        box=new JComboBox(address);//把选项添加进下拉列表


        String []education= {"高中","大专","本科","硕士","博士"};
        list=new JList(education);
        list.setVisibleRowCount(3);//设置单选款默认显示三个
        sp=new JScrollPane(list);//将列表添加进滚动条

        this.setLayout(new GridLayout(2,1));
        panel1.add(label1);
        panel1.add(box);
        panel2.add(label2);

        //panel2.add(list);
        panel2.add(sp);//直接添加这个，这与前面的buttongroup不同

        this.add(panel1);
        this.add(panel2);
        this.setTitle("用户调查");
        this.setSize(400, 190);
        this.setLocation(300, 200);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);



    }
    public static void main(String[] args) {
        下拉列表与滚动条 b=new 下拉列表与滚动条();
    }

}
