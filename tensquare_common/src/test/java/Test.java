import utils.IdWorker;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/8 9:29
 */
public class Test {
    public static void main(String[] args) {
        IdWorker idWorker = new IdWorker(1, 1);
        long nextId = idWorker.nextId();
        System.out.println(nextId);
    }
}
