package entity;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/8 9:15
 */
public class StatusCode {
    public static final int OK = 20000;// 成功
    public static final int ERROR = 20001;// 失败
    public static final int LOGINERROR = 20002;// 用户名或密码错误
    public static final int ACCESSERROR = 20003;// 权限不足
    public static final int REMOTEERROR = 20004;// 远程调用失败
    public static final int REPERROR = 20005;// 重复操作
}
