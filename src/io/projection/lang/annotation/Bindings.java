package io.projection.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specifiy a group of bindings (@Bind) if an attribute has
 * different binding with different classes.
 * 
 * @author Jorge Araújo
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Bindings {
	Bind[] value();
}
