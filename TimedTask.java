package cutDesktop;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by nss on 2019/5/15.
 */
public class TimedTask {
    //��ȡ��ǰ·��
    private static String path = System.getProperty("user.dir");
    private static PrintLog log = new PrintLog(path+"\\log\\");
    private static int num = 0;
    private static String style = ""; // ͼƬ����
    private static long switchingTime =  600000; //����ͼƬʱ��
    private static int cleanImgTime =  6; // ���ض�����ͼƬ�����old�ļ���
    private static String width = "1920"; //ͼƬ���
    private static String height = "1080"; //ͼƬ�߶�
    private static int grayscale = 0; //�Ƿ��ǻҶ�ͼƬ
    private static int blur = 0; //ģ�� ѡ�� 1-10
    //���õ�ַѡ��
    private static int standbyAddress = 1;

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
              @Override
              public void run() {

                  //���¼��������ļ� ͼƬ����
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
                  log.info("�滻�����ļ��ɹ� switchingTime = "+switchingTime+" style = "+style+" cleanImgTime = "+cleanImgTime+" width = "+width+" height ="+height+" standbyAddress="+standbyAddress+" path="+path+" grayscale="+grayscale);
                  switch (standbyAddress) {
                      case 1:
                          String[] styles= style.split("[,��]");
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
                      //ɾ�������ͼƬ
                      deleteImg(path+"\\old\\");
                      num = 0;
                  }else{
                      num++;
                  } }}, 0,switchingTime);
    }

    /**
     * ��ȡjar���������ļ�
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
            log.error("��ȡ������Ϣ����" + e.getMessage());
        }
        return data;
    }
    /**
     * ɾ���ļ�
     * @param address ��ַ
     */
    private static void deleteImg(String address) {
        File fileDir = new File(address);
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("jpg")) {
                    boolean isSu = file.delete();
                    if(!isSu){
                        log.error(file.getName()+"ɾ��ʧ�� path:"+address);
                    }
                }
            }
        }else{
            log.error("ɾ���ļ��Ҳ���·��");
        }
    }

    /**
     * ת���ļ�
     * @param address ��ַ
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
            log.error("ת���ļ�û�ҵ��ļ�");
        }
    }
    /**
     * ת�Ƶ��ļ�
     * @param startPath ת�ƵĿ�ʼĿ¼
     * @param fileName �ļ���
     * @param endPath ת�ƵĽ���Ŀ¼
     */
    private static HashMap<String,String> moveTotherFolders(String startPath, String fileName, String endPath){
        File startFile = new File(startPath +  fileName);
        File endFile = new File(endPath + fileName);
        HashMap<String, String> data = new HashMap<>();
        try {
            if(startFile.renameTo(endFile)){
                data.put("code","1");
                data.put("msg",fileName + " ת�Ƴɹ�");
            }else{
                data.put("code","-1");
                data.put("msg",fileName + " ת��ʧ�� startPath :"+startPath+fileName+" endPath :"+endPath+fileName);
            }
        } catch (Exception e) {
            data.put("code","-1");
            data.put("msg",fileName + " ת��ʧ��" + e.getMessage());
        }
        return data;
    }
}
