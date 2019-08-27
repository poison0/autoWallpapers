package cutDesktop;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by nss on 2019/5/15.
 */
public class TimedTask {
    //获取当前路径
    private static String path = System.getProperty("user.dir");
    private static PrintLog log = new PrintLog(path+"\\log\\");
    private static int num = 0;
    private static String style = ""; // 图片类型
    private static long switchingTime =  600000; //加载图片时间
    private static int cleanImgTime =  6; // 加载多少张图片后清除old文件夹
    private static String width = "1920"; //图片宽度
    private static String height = "1080"; //图片高度
    private static int grayscale = 0; //是否是灰度图片
    private static int blur = 0; //模糊 选择 1-10
    //备用地址选择
    private static int standbyAddress = 1;

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
              @Override
              public void run() {

                  //重新加载配置文件 图片类型
                  String[] config = {"types","switchingTime","cleanImgTime","width","height","standbyAddress","grayscale"};

                  HashMap<String,String> temps = readConfig(config);
                  if(temps.get("code").equals("1")){
                      style = temps.get("types");
                      switchingTime = Long.parseLong(temps.get("switchingTime"))*60000;
                      cleanImgTime = Integer.parseInt(temps.get("cleanImgTime"));
                      width = temps.get("width");
                      height = temps.get("height");
                      grayscale = Integer.parseInt(temps.get("grayscale"));
                      standbyAddress = Integer.parseInt(temps.get("standbyAddress"));
                  }
                  moveFiles(path+"\\");
                  log.info("替换配置文件成功 switchingTime = "+switchingTime+" style = "+style+" cleanImgTime = "+cleanImgTime+" width = "+width+" height ="+height+" standbyAddress="+standbyAddress+" path="+path+" grayscale="+grayscale);
                  switch (standbyAddress) {
                      case 1:
                          String[] styles= style.split("[,，]");
                          int n = (int)(Math.random()*styles.length);
                          new UploadDesktopImg(path+"\\" + sdf.format(new Date()) + ".jpg", "https://source.unsplash.com/random/" + width + "x" + height + "?" + styles[n],grayscale == 1).getImg();
                          break;
                      case 2:
                          new UploadDesktopImg(path+"\\" + sdf.format(new Date()) + ".jpg", "https://unsplash.it/" + width + "/" + height + "/?random",grayscale == 1).getImg();
                          break;
                      case 3: new UploadDesktopImg(path+"\\" + sdf.format(new Date()) + ".jpg", "https://bing.ioliu.cn/v1/rand?w="+width+"&h="+height,grayscale == 1).getImg();
                          break;
                      case 4: new UploadDesktopImg(path+"\\" + sdf.format(new Date()) + ".jpg", "http://lorempixel.com/"+width+"/"+height+"/",grayscale == 1).getImg();
                          break;
                      case 5: new UploadDesktopImg(path+"\\" + sdf.format(new Date()) + ".jpg", "https://picsum.photos/"+width+"/"+height+"/?random",grayscale == 1).getImg();
                          break;
                  }
                  if(num > cleanImgTime){
                      //删除保存的图片
                      deleteImg(path+"\\old\\");
                      num = 0;
                  }else{
                      num++;
                  } }}, 0,switchingTime);
    }

    /**
     * 读取jar包外配置文件
     */
    private static HashMap<String,String> readConfig(String[] keys){
        HashMap<String, String> data = new HashMap<>();
        Properties properties = new Properties();
        String filePath = System.getProperty("user.dir") + "/config/config.properties";
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(in);

            for (String key : keys) {
                String types = properties.getProperty(key);
                data.put(key, types);
            }
            data.put("code","1");
            in.close();
        } catch (IOException  e) {
            data.put("code","-1");
            log.error("读取配置信息出错！" + e.getMessage());
        }
        return data;
    }
    /**
     * 删除文件
     * @param address 地址
     */
    private static void deleteImg(String address) {
        File fileDir = new File(address);
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("jpg")) {
                    boolean isSu = file.delete();
                    if(!isSu){
                        log.error(file.getName()+"删除失败 path:"+address);
                    }
                }
            }
        }else{
            log.error("删除文件找不到路径");
        }
    }

    /**
     * 转移文件
     * @param address 地址
     */
    private static void moveFiles(String address){
        File fileDir = new File(address);
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("jpg")) {
                    HashMap<String , String> result = moveTotherFolders(address, file.getName(), address + "old\\");
                    if(result.get("code").equals("1")){
                        log.info(result.get("msg"));
                    }else{
                        log.error(result.get("msg"));
                    }
                }
            }
        }else {
            log.error("转移文件没找到文件");
        }
    }
    /**
     * 转移单文件
     * @param startPath 转移的开始目录
     * @param fileName 文件名
     * @param endPath 转移的结束目录
     */
    private static HashMap<String,String> moveTotherFolders(String startPath, String fileName, String endPath){
        File startFile = new File(startPath +  fileName);
        File endFile = new File(endPath + fileName);
        HashMap<String, String> data = new HashMap<>();
        try {
            if(startFile.renameTo(endFile)){
                data.put("code","1");
                data.put("msg",fileName + " 转移成功");
            }else{
                data.put("code","-1");
                data.put("msg",fileName + " 转移失败 startPath :"+startPath+fileName+" endPath :"+endPath+fileName);
            }
        } catch (Exception e) {
            data.put("code","-1");
            data.put("msg",fileName + " 转移失败" + e.getMessage());
        }
        return data;
    }
}
