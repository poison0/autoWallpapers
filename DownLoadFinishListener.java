package cutDesktop;

/**
 * Created by nss on 2019/5/14.
 */
public interface DownLoadFinishListener {
    /**
     * ���سɹ�
     */
    public void finish(String info);

    /**
     * ����ʧ��
     * @param error ʧ������
     */
    public void error(String error);
}
