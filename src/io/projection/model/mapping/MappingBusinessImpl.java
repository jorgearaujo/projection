package io.projection.model.mapping;

import io.projection.domain.Binding;
import io.projection.domain.Pair;
import io.projection.domain.Partnership;
import io.projection.model.Mapper;
import io.projection.model.Mapping;
import io.projection.util.ReflectionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.proxy.HibernateProxy;

public class MappingBusinessImpl implements MappingBusiness {

	private static String SEPARATOR = ".";

	private Mapping mapping;

	public MappingBusinessImpl(Mapping mapping) {
		this.mapping = mapping;
	}

	@Override
	public Object getTargetObject(Object origin, Binding binding) {
		Object target = instantiate(origin, binding);
		for (Partnership partnership : binding.getPartnerships()) {
			Pair originPair = null;
			Pair targetPair = null;
			if (origin.getClass().equals(binding.getOriginClass())) {
				originPair = getPair(origin, partnership.getOrigin(),
						TypeObject.ORIGIN);
				targetPair = getPair(target, partnership.getTarget(),
						TypeObject.TARGET);
			} else if (origin.getClass().equals(binding.getTargetClass())) {
				originPair = getPair(origin, partnership.getTarget(),
						TypeObject.ORIGIN);
				targetPair = getPair(target, partnership.getOrigin(),
						TypeObject.TARGET);
			}
			settleObject(originPair, targetPair);
		}
		return target;
	}

	private Object instantiate(Object origin, Binding binding) {
		Object returnObject = null;
		Class<?> classToInstantiate = null;
		if (origin.getClass().equals(binding.getOriginClass())) {
			classToInstantiate = binding.getTargetClass();
		} else if (origin.getClass().equals(binding.getTargetClass())) {
			classToInstantiate = binding.getOriginClass();
		}
		try {
			if (classToInstantiate != null) {
				returnObject = classToInstantiate.newInstance();
			}
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return returnObject;
	}

	private Pair getPair(Object obj, String value, TypeObject type) {
		Pair pair = new Pair();

		// If value exists and has no tokens, the value is set directly
		if (value != null && !value.contains(SEPARATOR)) {
			pair.setObject(obj);
			pair.setField(ReflectionUtil.getDeclaredField(obj.getClass(), value));
		}
		// If value exist but has tokens, then
		else if (value != null && value.contains(SEPARATOR)) {
			StringTokenizer tokenizer = new StringTokenizer(value, SEPARATOR);
			int numTokens = tokenizer.countTokens();
			String token = null;
			Class<?> currentClass = obj.getClass();

			Object pairObject = obj;
			while (numTokens > 1) {
				token = tokenizer.nextToken();
				Field field = ReflectionUtil.getDeclaredField(currentClass,
						token);
				if (type.equals(TypeObject.ORIGIN)) {
					pairObject = ReflectionUtil.fieldGet(field, pairObject);
				} else if (type.equals(TypeObject.TARGET)) {
					Object newInstance;
					if (ReflectionUtil.fieldGet(field, pairObject) != null) {
						newInstance = ReflectionUtil
								.fieldGet(field, pairObject);
					} else {
						newInstance = ReflectionUtil.instantiate(field
								.getType());
					}
					ReflectionUtil.fieldSet(field, pairObject, newInstance);
					pairObject = newInstance;
				}
				currentClass = field.getType();
				numTokens--;
			}
			token = tokenizer.nextToken();
			pair.setField(ReflectionUtil.getDeclaredField(currentClass, token));
			pair.setObject(pairObject);
		}
		return pair;
	}

	private Object settleObject(Pair originPair, Pair targetPair) {
		Field originField = originPair.getField();
		Field targetField = targetPair.getField();
		Object origin = originPair.getObject();
		Object target = targetPair.getObject();
		if (origin != null) {
			Boolean originFieldAccesible = ReflectionUtil
					.makeAccesible(originField);
			Boolean targetFieldAccesible = ReflectionUtil
					.makeAccesible(targetField);
			try {
				if (originField.get(origin) == null
						&& origin instanceof HibernateProxy) {
					Hibernate.initialize(origin);
					Object o = ((HibernateProxy) origin)
							.getHibernateLazyInitializer().getImplementation();
					origin = ((HibernateProxy) origin)
							.getHibernateLazyInitializer().getImplementation()
							.getClass().cast(o);
				}
			} catch (IllegalArgumentException e1) {
			} catch (IllegalAccessException e1) {
			} catch (HibernateException e1) {
			}
			Class<?> originFieldClass = getFieldClass(originField);
			Class<?> targetFieldClass = getFieldClass(targetField);
			try {
				// List -> List
				if (originFieldClass.equals(List.class)
						&& targetFieldClass.equals(List.class)) {
					handleList2List(origin, originField, target, targetField);
				}
				// Set -> Set
				else if (originFieldClass.equals(Set.class)
						&& targetFieldClass.equals(Set.class)) {
					handleSet2Set(origin, originField, target, targetField);
				}
				// List -> Set
				else if (originFieldClass.equals(List.class)
						&& targetFieldClass.equals(Set.class)) {
					handleList2Set(origin, originField, target, targetField);
				}
				// Set -> List
				else if (originFieldClass.equals(Set.class)
						&& targetFieldClass.equals(List.class)) {
					handleSet2List(origin, originField, target, targetField);
				}
				// List -> []
				else if (originFieldClass.equals(List.class)
						&& targetFieldClass.isArray()) {
					handleList2Array(origin, originField, target, targetField);
				}
				// Set -> []
				else if (originFieldClass.equals(Set.class)
						&& targetFieldClass.isArray()) {
					handleSet2Array(origin, originField, target, targetField);
				}
				// [] -> List
				else if (originFieldClass.isArray()
						&& targetFieldClass.equals(List.class)) {
					handleArray2List(origin, originField, target, targetField);
				}
				// [] -> Set
				else if (originFieldClass.isArray()
						&& targetFieldClass.equals(Set.class)) {
					handleArray2Set(origin, originField, target, targetField);
				}
				// [] -> []
				else if (originFieldClass.isArray()
						&& targetFieldClass.isArray()) {
					handleArray2Array(origin, originField, target, targetField,
							targetField.getType());
				}
				// Coinciden
				else {
					Boolean foundCoincidence = Boolean.FALSE;
					foundCoincidence = handleMapperList(origin, originField,
							target, targetField, originFieldClass,
							targetFieldClass, foundCoincidence);
					if (!foundCoincidence) {
						if (targetField.getType().equals(originFieldClass)) {
							handleCoincidence(origin, originField, target,
									targetField);
						} else {
							handleNonSpecifiedFormat(origin, originField,
									target, targetField, targetFieldClass);
						}
					}
				}
			} catch (IllegalAccessException e) {
			} catch (InstantiationException e) {
			} catch (NoSuchMethodException e) {
			} catch (Exception e) {
			}
			if (!originFieldAccesible) {
				ReflectionUtil.makeNoAccesible(originField);
			}
			if (!targetFieldAccesible) {
				ReflectionUtil.makeNoAccesible(targetField);
			}
		}

		return target;
	}

	private void handleNonSpecifiedFormat(Object inputInstance,
			Field inputField, Object outputInstance, Field outputField,
			Class<?> outputFieldClass) throws NoSuchMethodException,
			IllegalAccessException {
		Object subObjectInstance;
		subObjectInstance = mapping.map(inputField.get(inputInstance),
				outputFieldClass, outputField.get(outputInstance));
		outputField.set(outputInstance, subObjectInstance);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	Boolean handleMapperList(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField, Class<?> inputFieldClass,
			Class<?> outputFieldClass, Boolean foundCoincidence)
			throws Exception, IllegalAccessException {
		Object subObjectInstance = null;
		for (Mapper mapper : mapping.getMapperList()) {
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

			if (((inputFieldClass.equals(enclosingFrom) && (outputFieldClass
					.equals(to)))
					|| (inputFieldClass.equals(from) && (outputFieldClass
							.equals(enclosingTo)))
					|| (inputFieldClass.equals(enclosingFrom) && (outputFieldClass
							.equals(enclosingTo))) || (inputFieldClass
					.equals(from) && outputFieldClass.equals(to)))
					&& inputField.get(inputInstance) != null) {
				// Si es privado, lo hacemos público temporalmente
				Boolean fieldAccessible = inputField.isAccessible();
				if (!fieldAccessible) {
					inputField.setAccessible(true);
				}
				subObjectInstance = mapper.map(inputField.get(inputInstance));
				if (outputField.getType().equals(byte[].class)) {
					int j = 0;
					byte[] lowerBytes = new byte[((Byte[]) subObjectInstance).length];
					for (Byte b : (Byte[]) subObjectInstance) {
						lowerBytes[j++] = b.byteValue();
					}
					outputField.set(outputInstance, lowerBytes);

				} else {
					outputField.set(outputInstance, subObjectInstance);
				}
				// Se devuelve el campo a su nivel de accesibilidad original
				if (!fieldAccessible) {
					inputField.setAccessible(false);
				}
				foundCoincidence = Boolean.TRUE;
				break;
			}
		}
		return foundCoincidence;
	}

	@SuppressWarnings("unchecked")
	private <E> void handleArray2Array(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField, E cl)
			throws IllegalAccessException {
		Class<?> c = outputField.getType();
		E[] outputArray = (E[]) Array.newInstance(c.getComponentType(),
				((E[]) inputField.get(inputInstance)).length);
		int i = 0;
		for (Object object : (E[]) inputField.get(inputInstance)) {
			E o = (E) mapping.map(object, c.getComponentType());
			outputArray[i++] = o;
		}
		outputField.set(outputInstance, outputArray);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleArray2Set(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		Object arrayInstance = inputField.get(inputInstance);
		ParameterizedType listType = (ParameterizedType) outputField
				.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		Set<E> list = new HashSet<E>();
		for (E object : (E[]) arrayInstance) {
			list.add((E) mapping.map(object, listClass));
		}
		outputField.set(outputInstance, list);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleArray2List(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		Object arrayInstance = inputField.get(inputInstance);
		ParameterizedType listType = (ParameterizedType) outputField
				.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		List<E> list = new ArrayList<E>();
		for (E object : (E[]) arrayInstance) {
			list.add((E) mapping.map(object, listClass));
		}
		outputField.set(outputInstance, list);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleSet2Array(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		Class<?> c = outputField.getType();
		E[] outputArray = (E[]) Array.newInstance(c.getComponentType(),
				((Set<E>) inputField.get(inputInstance)).size());
		int i = 0;
		for (E object : (Set<E>) inputField.get(inputInstance)) {
			E o = (E) mapping.map(object, c.getComponentType());
			outputArray[i++] = o;
		}
		outputField.set(outputInstance, outputArray);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleList2Array(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException, InstantiationException {
		Class<?> c = outputField.getType();
		E[] outputArray = (E[]) Array.newInstance(c.getComponentType(),
				((List<E>) inputField.get(inputInstance)).size());
		int i = 0;
		for (E object : (List<E>) inputField.get(inputInstance)) {
			E o = (E) mapping.map(object, c.getComponentType());
			outputArray[i++] = o;
		}
		outputField.set(outputInstance, outputArray);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleSet2List(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		ParameterizedType listType = (ParameterizedType) outputField
				.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		List<E> list = new ArrayList<E>();
		for (E object : ((Set<E>) inputField.get(inputInstance))) {
			E obj = (E) mapping.map(object, listClass);
			list.add(obj);
		}
		outputField.set(outputInstance, list);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleList2Set(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		ParameterizedType listType = (ParameterizedType) outputField
				.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		Set<E> list = new HashSet<E>();
		for (E object : ((List<E>) inputField.get(inputInstance))) {
			E obj = (E) mapping.map(object, listClass);
			list.add(obj);
		}
		outputField.set(outputInstance, list);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleSet2Set(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		ParameterizedType listType = (ParameterizedType) outputField
				.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		Set<E> list = new HashSet<E>();
		for (E object : ((Set<E>) inputField.get(inputInstance))) {
			E obj = (E) mapping.map(object, listClass);
			list.add(obj);
		}
		outputField.set(outputInstance, list);
	}

	@SuppressWarnings("unchecked")
	private <E> void handleList2List(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		ParameterizedType listType = (ParameterizedType) outputField
				.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		List<E> list = new ArrayList<E>();
		for (E object : ((List<E>) inputField.get(inputInstance))) {
			E obj = (E) mapping.map(object, listClass);
			list.add(obj);
		}
		outputField.set(outputInstance, list);
	}

	private void handleCoincidence(Object inputInstance, Field inputField,
			Object outputInstance, Field outputField)
			throws IllegalAccessException {
		try {
			if (ReflectionUtil.isBoxedPrimitive(inputField.getType())) {
				outputField.set(outputInstance, inputField.get(inputInstance));
			} else {
				Object toMap = mapping.map(inputField.get(inputInstance),
						outputField.getType(), outputInstance);
				outputField.set(outputInstance, toMap);
			}
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("rawtypes")
	private Class getFieldClass(Field field) {
		Class fieldClass = null;
		if (field.getType().equals(long.class)) {
			fieldClass = Long.class;
		} else if (field.getType().equals(boolean.class)) {
			fieldClass = Boolean.class;
		} else if (field.getType().equals(int.class)) {
			fieldClass = Integer.class;
		} else if (field.getType().equals(double.class)) {
			fieldClass = Double.class;
		} else if (field.getType().equals(float.class)) {
			fieldClass = Float.class;
		} else if (field.getType().equals(byte.class)) {
			fieldClass = Byte.class;
		} else if (field.getType().equals(byte[].class)) {
			fieldClass = Byte[].class;
		} else if (field.getType().equals(long[].class)) {
			fieldClass = Long[].class;
		} else if (field.getType().equals(boolean[].class)) {
			fieldClass = Boolean[].class;
		} else if (field.getType().equals(int[].class)) {
			fieldClass = Integer[].class;
		} else if (field.getType().equals(double[].class)) {
			fieldClass = Double[].class;
		} else if (field.getType().equals(float[].class)) {
			fieldClass = Float[].class;
		} else if (field.getType().equals(char.class)) {
			fieldClass = Character.class;
		} else if (field.getType().equals(char[].class)) {
			fieldClass = Character[].class;
		} else if (field.getType().equals(short.class)) {
			fieldClass = Short.class;
		} else if (field.getType().equals(short[].class)) {
			fieldClass = Short[].class;
		} else {
			fieldClass = field.getType();
		}
		return fieldClass;
	}

}
