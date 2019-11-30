package self.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Controller
 *
 * @author chenzb
 * @date 2019/11/26
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String value() default "";
}
