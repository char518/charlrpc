import com.charl.DemoApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import piggy.call.client.ClientProxy;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.debug("start up application!!!");
        AnnotationConfigApplicationContext aca = new AnnotationConfigApplicationContext(BeanConfig.class);

        ClientProxy clientProxy = (ClientProxy) aca.getBean("clientProxy");
        DemoApi proxy = (DemoApi) clientProxy.createProxy(DemoApi.class);

        String client_say_bye = proxy.bye("client bye");
        System.out.println(client_say_bye);
        System.exit(0);
    }

}