package cutDesktop;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nss on 2019/5/15.
 */
class PrintLog {
    private String logAddress = "";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

     PrintLog(String logAddress) {
        this.logAddress = logAddress;
    }

    void info(String info){
        File fileDir = new File(logAddress);
        String date = sdf.format(new Date());
        if(!fileDir.isDirectory()){
            fileDir.mkdir();
        }
        File[] files = fileDir.listFiles();
        boolean isConflict = false;
        for (File file : files) {
            if (file.getName().contains(date)) {
                isConflict = true;
            }
        }
        if (!isConflict) {
            File currentFileInfo = new File(logAddress+date+".Info.log");
            try {
                currentFileInfo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            out = new FileOutputStream(new File(logAddress+date+".Info.log"),true);
            out.write((sdfs.format(new Date())+":"+info+"\r\n").getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void error(String error){
        File fileDir = new File(logAddress);
        String date = sdf.format(new Date());
        if(!fileDir.isDirectory()){
            fileDir.mkdirs();
        }
        File[] files = fileDir.listFiles();
        boolean isConflict = false;
        for (File file : files) {
            if (file.getName().contains(date)) {
                isConflict = true;
            }
        }
        if (!isConflict) {
            File currentFileInfo = new File(logAddress+date+".Error.log");
            try {
                currentFileInfo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out;
        try {
            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            out = new FileOutputStream(new File(logAddress+date+".Error.log"),true);
            out.write((sdfs.format(new Date())+":"+error+"\r\n").getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
