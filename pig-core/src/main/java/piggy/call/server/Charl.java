package piggy.call.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Charl {

    /**
     * 服务接口
     * @return
     */
    Class<?> value();

    /**
     * 版本号
     * @return
     */
    String version() default "";

}