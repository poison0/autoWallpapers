package cutDesktop;

/**
 * Created by nss on 2019/5/14.
 */
public interface DownLoadFinishListener {
    /**
     * 下载成功
     */
    public void finish(String info);

    /**
     * 下载失败
     * @param error 失败内容
     */
    public void error(String error);
}
