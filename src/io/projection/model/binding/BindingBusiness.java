package io.projection.model.binding;

import io.projection.domain.Binding;

public interface BindingBusiness {

	/**
	 * Gets the binding between two classes.
	 * 
	 * @param originClass
	 *            Class origin of mapping
	 * @param targetClass
	 *            Class target of mapping
	 * @param cache
	 *            Indicates if cache is activated
	 * 
	 * @see io.projection.Binding
	 * @return Bindings between classes
	 */
	public Binding getBinding(Class<?> originClass, Class<?> targetClass,
			Boolean cache);

}
