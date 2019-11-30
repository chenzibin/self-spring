package self.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Controller
 *
 * @author chenzb
 * @date 2019/11/26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Repository {

    String value();
}
