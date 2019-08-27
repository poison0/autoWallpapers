package cutDesktop;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nss
 * @description 图片转换为灰度
 * @date 2019/8/6
 */
public class ImageGrayscale {
   static void transition(File file,DownLoadFinishListener listener){
        BufferedImage image = null;
//        File file = null;
//        file = new File(startAddress);
        try {
            image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int p = image.getRGB(i, j);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    int avg = (r + g + b) / 3;
                    p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                    image.setRGB(i, j, p);
                }
            }
//            file = new File(endAddress);
            ImageIO.write(image, "jpg", file);
            listener.finish("转换灰度成功");
        } catch (IOException e) {
            e.printStackTrace();
            listener.error("转换灰度出错"+e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        transition("d:\\2019-08-06 20-12-07.jpg","d:\\a.jpg",new DownLoadFinishListener(){
//
//            @Override
//            public void finish(String info) {
//                log.info(info);
//            }
//
//            @Override
//            public void error(String error) {
//                log.error(error);
//            }
//        });
//    }
}
