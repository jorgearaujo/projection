package io.projection.model.binding;

import io.projection.domain.Binding;

public interface BindingDao {

	/**
	 * Gets a binding based on origin and target class.
	 * 
	 * @param originClass
	 * @param targetClass
	 * @return Binding
	 */
	public Binding get(Class<?> originClass, Class<?> targetClass);

	/**
	 * Save the binding to the static memory.
	 * 
	 * @param binding
	 *            Binding to be saved
	 */
	public void save(Binding binding);
}
