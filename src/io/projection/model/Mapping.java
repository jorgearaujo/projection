package io.projection.model;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface Mapping {

	/**
	 * Creates a new object of the specified target class based on origin object
	 * data. The mapping is based on @Bind annotations put on origin or target
	 * classes, if an attribute is not binded with other attribute, it is
	 * automatically binded by name (if possible). See more on Projection API.
	 * 
	 * @see http://projection.io/api
	 * 
	 * @param origin
	 *            Object from which output object is generated
	 * @param target
	 *            Class of the output object
	 * @return A new instance of the specified target class
	 */
	Object map(Object origin, Class target);

	/**
	 * Creates a new object of the specified target class based on origin object
	 * data and starting with the targetInstance indicated. The mapping is based
	 * on @Bind annotations put on origin or target classes, if an attribute is
	 * not binded with other attribute, it is automatically binded by name (if
	 * possible). See more on Projection API.
	 * 
	 * @see http://projection.io/api
	 * 
	 * @param origin
	 *            Object from which output object is generated
	 * @param target
	 *            Class of the output object
	 * @param targetInstance
	 *            Starting instance from final output instance will be created
	 * @return A new instance of the specified target class
	 */
	Object map(Object origin, Class target, Object targetInstance);

	/**
	 * Specified if cache is enabled. If it is activated, future mappings
	 * between same classes will be faster, but first mapping will be slower.
	 * So, cache is suitable if a mapping will be done a lot of times, and is
	 * not suitable if the mappings are between a lot of classes and repeated
	 * only one time.
	 * 
	 * @return true if binding cache is enabled, false otherwise
	 */
	Boolean isCacheEnabled();

	/**
	 * List of mappers added to the Projection mapping.
	 * 
	 * @return List of mappers
	 */
	List<Mapper> getMapperList();
}
