package image_comparison;

import ZLYUtils.JavaUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ImageComparison {
    private static final File F1 = new File("QQ截图20180822114237.png");
    private static final File F2 = new File("QQ截图20180822114237.png");

    /**
     * 获取本地已保存的文件
     *
     * @param f
     * @return
     */
    public static BufferedImage getImageFromFile(File f) {
        BufferedImage img = null;
        try {
            img = javax.imageio.ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * 进行图片对比
     *
     * @param myImage
     * @param otherImage
     * @param percent
     * @return
     */
    private static boolean sameAs(BufferedImage myImage, BufferedImage otherImage, double percent) {
        int width = myImage.getWidth();
        int height = myImage.getHeight();
        int numDiffPixels = 0;
        BufferedImage bufferedImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = (int) (height * 0.2); y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (myImage.getRGB(x, y) != otherImage.getRGB(x, y)) {
                    numDiffPixels++;
                }
                bufferedImage.setRGB(x, y, myImage.getRGB(x, y));
            }
        }
        double numberPixels = height * width;
        double diffPercent = (numDiffPixels * 1.0) / numberPixels;
        try {
            ImageIO.write(bufferedImage, "png", new File("123.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return percent <= (1.0D - diffPercent) * 100;
    }

    /**
     * 进行手机图片对比
     *
     * @param myImage
     * @param otherImage
     * @return
     */
    private static boolean sameAs(BufferedImage myImage, BufferedImage otherImage) {
        int width = myImage.getWidth();
        int height = myImage.getHeight();
        boolean b = true;
        BufferedImage bufferedImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = (int)(myImage.getHeight()*0.04); y < height; y++) {
            for (int x = 0; x < width; x++) {
                bufferedImage.setRGB(x,y, otherImage.getRGB(x, y));
                if (myImage.getRGB(x, y) != otherImage.getRGB(x, y)) {
                    bufferedImage.setRGB(x, y, 16777216);
                    b = false;
                } else {
                    bufferedImage.setRGB(x, y, -1);
                }
            }
        }
        if (!b) {
            try {
                ImageIO.write(bufferedImage, "png", new File("12345.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return b;
    }

    /**
     * 获取手机图片对比结果
     * 比对时去除顶部状态栏
     *
     * @return 成功返回"success"，失败返回具体原因
     */
    public static String phoneImageComparison(File image1, File image2) {
        try {
            if (!"success".equals(checkImageLegal(image1, image2)))
                return checkImageLegal(image1, image2);
            BufferedImage img1 = getImageFromFile(image1);
            BufferedImage img2 = getImageFromFile(image2);
            if (img1.getWidth() != img2.getWidth()) return "宽度不匹配";

            if (img1.getHeight() != img2.getHeight()) return "高度不匹配";
            if (!sameAs(img1, img2)) return "像素不匹配";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return "success";

    }

    /**
     * 检查图片是否合法
     */
    private static String checkImageLegal(File image1, File image2) {
        if (!"success".equals(checkImageLegal(image1)))
            return (checkImageLegal(image1));
        if (!"success".equals(checkImageLegal(image2)))
            return (checkImageLegal(image2));
        try {
            if (!JavaUtils.getFileSuffixName(image1).equals(
                    JavaUtils.getFileSuffixName(image2)
            )) return "两张图片格式不相同";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return "success";
    }

    /**
     * 检查图片是否合法
     */
    private static String checkImageLegal(File image) {
        if (image == null)
            return "image为空";
        if (!image.exists())
            return ("image图片不存在" + image.getPath());
        if (!checkImageNameSuffix(image))
            return ("image图片格式不支持" + image.getName());
        return "success";
    }


    /**
     * 检查图片后缀名是否为图片
     *
     * @param image
     * @return
     */
    private static boolean checkImageNameSuffix(File image) {
        if (image == null) throw
                new IllegalArgumentException("image为空");
        if (!image.exists()) throw
                new IllegalArgumentException("image图片不存在:" + image.getPath());
        if (image.getName().indexOf(".") == -1) throw
                new IllegalArgumentException("image后缀名不正确" + image.getName());
        for (String imageSuffix : Image_comparison_config.IMAGE_SUFFIX) {
            if (image.getName().endsWith(imageSuffix)) return true;
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(phoneImageComparison(new File("1.png"),
                new File("2.png")));
    }
}
