package org.rulez.demokracia.pdengine.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test.None;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.METHOD
})

public @interface Contract {

  String value();

  Class<? extends Throwable> expected() default None.class;

  long timeout() default 0L;
}
