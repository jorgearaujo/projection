package io.projection.model.mapping;

import io.projection.domain.Binding;

public interface MappingBusiness {

	/**
	 * Obtains the object mapped with the specified origin object and the
	 * specified bindings.
	 * 
	 * @param origin
	 *            Object to be mapped with.
	 * @param binding
	 *            Relation between classes and attributes.
	 * @return Mapped object of type specified by binding argument
	 */
	public Object getTargetObject(Object origin, Binding binding);

}
