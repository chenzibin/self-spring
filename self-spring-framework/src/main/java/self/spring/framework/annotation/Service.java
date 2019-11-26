package self.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Service
 *
 * @author chenzb
 * @date 2019/11/26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value();
}
