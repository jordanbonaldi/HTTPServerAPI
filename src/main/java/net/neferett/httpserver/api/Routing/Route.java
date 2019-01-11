package net.neferett.httpserver.api.Routing;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

    String name() default "";

    String[] params() default {};

    boolean activated() default true;
}
