package piggy.common;

public class ServiceInstance {

    private String serviceName;

    private String host;

    private String port;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" + "serviceName='" + serviceName + '\'' + ", host='" + host + '\'' + ", port='" + port + '\'' + '}';
    }
}
