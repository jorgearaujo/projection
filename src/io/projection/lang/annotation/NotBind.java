package io.projection.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify if an attribute hasn't to be binded with any
 * attribute of the class specified by with.
 * 
 * @author Jorge Araújo
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@SuppressWarnings("rawtypes")
public @interface NotBind {
	Class with();
}
