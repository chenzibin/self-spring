package self.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Controller
 *
 * @author chenzb
 * @date 2019/11/26
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value();
}
