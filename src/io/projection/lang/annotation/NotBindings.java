package io.projection.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify a group of not bindings (@NotBind), if an
 * attribute has multiple classes to don't be binded with.
 * 
 * @author Jorge
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface NotBindings {
	NotBind[] value();
}
