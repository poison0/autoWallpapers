package cutDesktop;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nss on 2019/5/14.
 */
public  class DownLoadService {
    /**
     * 下载图片链接
     * @param address 地址
     * @param listener 回调类
     */
    public static void doGet(String address,RequestFinishListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                BufferedReader br = null;
                try {
                    URL url = new URL(address);
                    connection =(HttpURLConnection)url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(3000);
                    connection.connect();
                    inputStream = connection.getInputStream();
                    br = new BufferedReader(new InputStreamReader(inputStream));
                    String str = null;
                    StringBuilder readCon = new StringBuilder();
                    while ((str = br.readLine()) != null) {
                        readCon.append(str);
                    }
                    if (listener != null) {
                        listener.success(readCon.toString());
                    }
                    br.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.error(e.getMessage());
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     *  获取图片
     * @param saveAddress 保存地址
     * @param imgUrl 图片地址
     * @param listener 回调
     */
     static void getImg(String saveAddress,String imgUrl,boolean isGray,DownLoadFinishListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream in;
                FileOutputStream out;
                try {
                    File file = new File(saveAddress);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    out = new FileOutputStream(file);
                    URL url = new URL(imgUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(5000);
                    in = connection.getInputStream();
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer,0,len);
                    }
                    in.close();
                    out.flush();
                    out.close();
                    if (isGray) {
                        ImageGrayscale.transition(file, new DownLoadFinishListener() {
                            @Override
                            public void finish(String info) {
                                listener.finish(info);
                            }
                            @Override
                            public void error(String error) {
                                listener.error(error);
                            }
                        });
                    }
                    if (listener != null) {
                        listener.finish("下载成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.error(e.getMessage());
                    }
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
