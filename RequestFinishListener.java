package cutDesktop;

/**
 * Created by nss on 2019/5/14.
 * ����������ɵ�
 */
public interface RequestFinishListener {
    /**
     * ����ɹ����
     * @param result ���
     */
     void success(String result);

    /**
     * ����ʧ��
     * @param error ʧ������
     */
     void error(String error);

}
