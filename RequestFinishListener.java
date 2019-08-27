package cutDesktop;

/**
 * Created by nss on 2019/5/14.
 * 监听请求完成的
 */
public interface RequestFinishListener {
    /**
     * 请求成功完成
     * @param result 结果
     */
     void success(String result);

    /**
     * 请求失败
     * @param error 失败内容
     */
     void error(String error);

}
