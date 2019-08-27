package cutDesktop;

/**
 * Created by nss on 2019/5/14.
 */
class UploadDesktopImg {
    private static String path = System.getProperty("user.dir");
    private PrintLog log = new PrintLog(path+"\\log\\");
    private String address;
    private String source;
    private Boolean isGray;


     UploadDesktopImg(String address,String source,Boolean isGray) {
        this.address = address;
        this.source = source;
        this.isGray = isGray;
    }
     void getImg(){
        DownLoadService.getImg(address,source,isGray,new DownLoadFinishListener(){

            @Override
            public void finish(String info) {
                log.info(info);
            }

            @Override
            public void error(String error) {
                log.error(error);
            }
        });
    }

}
