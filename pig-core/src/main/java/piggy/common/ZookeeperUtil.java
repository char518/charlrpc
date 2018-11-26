package piggy.common;

import org.I0Itec.zkclient.ZkClient;

public class ZookeeperUtil {

    private static ZkClient zkClient;

    public static ZkClient connect() {
        zkClient = new ZkClient(Constants.ZK_HOST, Constants.SESSION_TIME_OUT, Constants.CONNECT_TIME_OUT);
        return zkClient;
    }

    public static void close() {
        if (null == zkClient) {
            return;
        }
        zkClient.close();
    }
}
