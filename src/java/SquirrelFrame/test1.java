package SquirrelFrame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * <p>
 * Title: 史上最扯Java图形绘制（J2SE）教程之一,图像变换。
 * </p>
 * <p>
 * Description:图像变换
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class test1 extends Frame {

    /**
     * 这个例子我用AWT来讲解，实际上，我们都知道使用Swing时，对图 像绘制上有很多的优化，但是在AWT中，这些都是要我们手动来做得，
     * 所以入门我还是以AWT开始。（其实大家都知道，组件名加个J就变 Swing了……几乎不用改别的。）
     */

    private static final long serialVersionUID = 1L;

    // 用于存储图片
    Image act;

    // 俗称的帆布啦，做手机开发的话很常见
    Canvas canvas = new Canvas() {

        private static final long serialVersionUID = 1L;

        /**
         * 绘制图像，直接调用update方法（顺便说一下，若以paint调用update,
         * 再用update调用paint，会出现很好玩的事。^^）
         */
        public void paint(Graphics g) {
            update(g);
        }

        /**
         * 此方法仅在在发生变更时绘制图形
         */
        public void update(Graphics g) {
            // 参数分别为加载图像，所在Graphics上的left,所在top,图像width,图像height,目标对象
            g.drawImage(act, getWidth() / 2 - act.getWidth(null) / 2,
                    getHeight() / 2 - act.getHeight(null) / 2, act
                            .getWidth(null), act.getHeight(null), this);
        }
    };

    /**
     * 简单动画的构造
     *
     */
    public test1() {
        setTitle("图像变换");
        setSize(400, 400);
        // 设定背景为黑色
        setBackground(Color.BLACK);
        // 直接以class路径读取图片资源
        URL imageUrl = getClass().getResource("image/logo.png");
        // 获取image图像实体
        act = Toolkit.getDefaultToolkit().createImage(imageUrl);
        // 加载帆布作为背景
        add(canvas);
        class AnimeMouseListener extends MouseAdapter {
            // 鼠标进入变更
            public void mouseEntered(MouseEvent e) {
                // 顺便说一下，水平反转图的效果，是可以用代码做到的，后面讲解。
                URL imageUrl = getClass().getResource("image/RunMainPerformance.png");
                act = Toolkit.getDefaultToolkit().createImage(imageUrl);
                canvas.repaint();
            }

            // 鼠标离开变更
            public void mouseExited(MouseEvent e) {
                URL imageUrl = getClass().getResource("image/refresh.png");
                act = Toolkit.getDefaultToolkit().createImage(imageUrl);
                canvas.repaint();
            }
        }
        //在帆布上添加鼠标监听
        canvas.addMouseListener(new AnimeMouseListener());
    }

    /**
     * 主函数
     *
     * @param args
     */
    public static void main(String[] args) {
        // 实例化本类
        test1 sa = new test1();
        // 显示窗体
        sa.setVisible(true);
    }
}