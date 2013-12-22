package io.projection.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to bind attributes. If an attribute is annotated with @Bind,
 * then it has a relation with the attribute with name specified by value and
 * class specified by with. If inside is applied, then the attributes inside the
 * annotated attribute are binded, not itself.
 * 
 * @author Jorge Araújo
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@SuppressWarnings("rawtypes")
public @interface Bind {
	Class with();

	String value() default "";

	boolean inside() default false;
}
