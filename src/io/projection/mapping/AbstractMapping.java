package io.projection.mapping;

import io.projection.domain.Binding;
import io.projection.model.Mapper;
import io.projection.model.Mapping;
import io.projection.model.binding.BindingBusinessFactory;
import io.projection.model.mapping.MappingBusinessFactory;
import io.projection.util.ReflectionUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
abstract class AbstractMapping implements Mapping {

	private List<Mapper> mapperList;

	@Override
	public Object map(Object origin, Class targetClass) {
		return map(origin, targetClass, null);
	}

	@Override
	public Object map(Object origin, Class targetClass, Object target) {
		Object returnedObject;
		
		// If origin is null, then returned object is also null
		if (origin == null) {
			returnedObject = null;
		}
		// Check if is a mapped defined for the types. If exists, then the
		// object mapped is returned directly by the method.
		Object obj = checkMapper(origin, targetClass);
		if (obj != null) {
			returnedObject = obj;
		}

		// Calculates the bindings between the classes
		Binding binding = BindingBusinessFactory.getInstance().getBinding(
				origin.getClass(), targetClass, isCacheEnabled());
		// Map origin to target using the bindings
		obj = MappingBusinessFactory.getInstance(this).getTargetObject(origin,
				binding);
		returnedObject = obj;
		
		// Returns object
		return returnedObject;
	}

	@SuppressWarnings("unchecked")
	private Object checkMapper(Object origin, Class targetClass) {
		Object obj = null;
		for (Mapper mapper : getMapperList()) {
			if (matchClasses(mapper, origin.getClass(), targetClass)) {
				try {
					obj = mapper.map(origin);
				} catch (Exception e) {
				}
				break;
			}
		}
		return obj;
	}

	@Override
	public Boolean isCacheEnabled() {
		return Boolean.TRUE;
	}

	@Override
	public List<Mapper> getMapperList() {
		return mapperList == null ? mapperList = new ArrayList<Mapper>()
				: mapperList;
	}

	/**
	 * Adds a new custom mapper to the Projection mapping.
	 * 
	 * @param mapper
	 *            Custom mapper which implements Mapper
	 * 
	 * @see io.projection.Mapper
	 */
	protected void add(Mapper mapper) {
		getMapperList().add(mapper);
	}

	/**
	 * Adds all classes which implements Mapper from the specified package.
	 * 
	 * @param pkg
	 *            Package name where mappers are implemented.
	 */
	protected void setPackage(String pkg) {
		List<Class> classList = ReflectionUtil.getClassesForPackage(pkg);
		for (Class class1 : classList) {
			try {
				if (Mapper.class.isAssignableFrom(class1)) {
					getMapperList().add((Mapper) class1.newInstance());
				}
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}

	private Boolean matchClasses(Mapper mapper, Class<?> originClass,
			Class<?> targetClass) {
		Boolean foundCoincidence = Boolean.FALSE;
		Type[] genericInterfaces = mapper.getClass().getGenericInterfaces();

		Class from = null;
		Class enclosingFrom = null;
		try {
			from = (Class<?>) ((ParameterizedType) genericInterfaces[0])
					.getActualTypeArguments()[0];
		} catch (ClassCastException e) {
			enclosingFrom = (Class) ((ParameterizedType) ((ParameterizedType) genericInterfaces[0])
					.getActualTypeArguments()[0]).getRawType();
			ParameterizedType tempFrom = (ParameterizedType) ((ParameterizedType) genericInterfaces[0])
					.getActualTypeArguments()[0];
			from = (Class) tempFrom.getActualTypeArguments()[0];
		}

		Class to = null;
		Class enclosingTo = null;
		try {
			to = (Class<?>) ((ParameterizedType) genericInterfaces[0])
					.getActualTypeArguments()[1];
		} catch (ClassCastException e) {
			enclosingTo = (Class) ((ParameterizedType) ((ParameterizedType) genericInterfaces[0])
					.getActualTypeArguments()[1]).getRawType();
			ParameterizedType tempTo = (ParameterizedType) ((ParameterizedType) genericInterfaces[0])
					.getActualTypeArguments()[1];
			to = (Class) tempTo.getActualTypeArguments()[0];
		}

		if ((originClass.equals(from) && targetClass.equals(to))
				|| (originClass.equals(enclosingFrom) && targetClass.equals(to))
				|| (originClass.equals(enclosingFrom) && targetClass
						.equals(enclosingTo))
				|| (originClass.equals(from) && targetClass.equals(enclosingTo))) {
			foundCoincidence = Boolean.TRUE;
		}
		return foundCoincidence;
	}
}
