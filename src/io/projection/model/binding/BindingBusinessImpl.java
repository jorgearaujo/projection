package io.projection.model.binding;

import io.projection.domain.Binding;
import io.projection.domain.Partnership;
import io.projection.lang.annotation.Bind;
import io.projection.lang.annotation.Bindings;
import io.projection.lang.annotation.NotBind;
import io.projection.lang.annotation.NotBindings;
import io.projection.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BindingBusinessImpl implements BindingBusiness {

	private static final String SEPARATOR = ".";

	@Override
	public Binding getBinding(Class<?> originClass, Class<?> targetClass,
			Boolean cache) {
		Binding binding = null;

		// If cache is enabled, then search if binding was already made
		if (cache) {
			binding = BindingDaoFactory.getInstance().get(originClass,
					targetClass);
		}

		// If binding is not found in the cache, then create it from the classes
		if (binding == null) {
			binding = generateBinding(originClass, targetClass);
			// If cache is enabled, the current binding is saved to it
			if (cache) {
				BindingDaoFactory.getInstance().save(binding);
			}
		}

		return binding;
	}

	private Binding generateBinding(Class<?> originClass, Class<?> targetClass) {
		Binding binding = new Binding();
		binding.setOriginClass(originClass);
		binding.setTargetClass(targetClass);

		// STEP 1: Look for @Bind annotations into de target and origin class
		binding.getPartnerships().addAll(
				getBindByAnnotation(originClass, targetClass,
						Position.ORIGIN_TARGET));
		binding.getPartnerships().addAll(
				getBindByAnnotation(targetClass, originClass,
						Position.TARGET_ORIGIN));

		// STEP 2: Look for name coincidences with fields that are not already
		// binded. It's important to know that not annotated fields are
		// automatically mapped.
		binding.getPartnerships().addAll(
				getBindByName(targetClass, originClass,
						binding.getPartnerships()));
		binding.getPartnerships().addAll(
				getBindByName(originClass, targetClass,
						binding.getPartnerships()));

		return binding;

	}

	private List<Partnership> getBindByName(Class<?> classToInspect,
			Class<?> classToCompare, List<Partnership> currentBindings) {
		List<Partnership> returnMap = new ArrayList<Partnership>();
		List<Field> inspectedFields = ReflectionUtil
				.getAllFields(classToInspect);
		for (Field inspectedField : inspectedFields) {
			String value = inspectedField.getName();
			if (getNotBindAnnotation(inspectedField, classToCompare) == null
					&& ReflectionUtil.existsField(classToCompare, value)
					&& getNotBindAnnotation(ReflectionUtil.getDeclaredField(
							classToCompare, value), classToInspect) == null
					&& !isAlreadyMapped(currentBindings, value)) {
				returnMap.add(new Partnership(value, value));
			}
		}
		return returnMap;
	}

	private List<Partnership> getBindByAnnotation(Class<?> classToInspect,
			Class<?> classToCompare, Position position, String startingString) {
		List<Partnership> returnMap = new ArrayList<Partnership>();
		List<Field> inspectedFields = ReflectionUtil
				.getAllFields(classToInspect);
		// Loop with all field by the parametrized class
		for (Field inspectedField : inspectedFields) {
			// Gets all @Bind annotations
			List<Bind> bindList = getBindAnnotations(inspectedField,
					classToCompare);
			// If there are @Bind annotations on field
			if (!bindList.isEmpty()) {
				for (Bind bind : bindList) {
					// If binding is inside type, call here recursively adding
					// 'subFields'
					if (bind.inside()) {
						returnMap.addAll(getBindByAnnotation(
								inspectedField.getType(), classToCompare,
								position,
								startingString + inspectedField.getName()
										+ SEPARATOR));

					}
					// Else if binding is value type, insert
					else if (!bind.value().isEmpty()) {
						String originValue = null;
						String targetValue = null;
						if (position.equals(Position.ORIGIN_TARGET)) {
							originValue = startingString
									+ inspectedField.getName();
							targetValue = bind.value();
						} else if (position.equals(Position.TARGET_ORIGIN)) {
							targetValue = startingString
									+ inspectedField.getName();
							originValue = bind.value();
						}
						// Only if field exist and it is not duplicated
						if (ReflectionUtil.existsField(classToCompare,
								bind.value(), SEPARATOR)
								&& !isDuplicated(returnMap, originValue,
										targetValue)) {
							returnMap.add(new Partnership(originValue,
									targetValue));
						}
					}
				}
			}
		}
		return returnMap;
	}

	private List<Partnership> getBindByAnnotation(Class<?> classToInspect,
			Class<?> classToCompare, Position position) {
		return getBindByAnnotation(classToInspect, classToCompare, position, "");
	}

	private Boolean isDuplicated(List<Partnership> currentBindings,
			String originValue, String targetValue) {
		Boolean returnValue = Boolean.FALSE;
		for (Partnership partnership : currentBindings) {
			if (partnership.getOrigin().equals(originValue)
					&& partnership.getTarget().equals(targetValue)) {
				returnValue = Boolean.TRUE;
				break;
			}
		}
		return returnValue;
	}

	private Boolean isAlreadyMapped(List<Partnership> currentBindings,
			String value) {
		Boolean returnValue = Boolean.FALSE;
		for (Partnership partnership : currentBindings) {
			if (partnership.getOrigin().equals(value)
					|| partnership.getTarget().equals(value)) {
				returnValue = Boolean.TRUE;
				break;
			}
		}
		return returnValue;
	}

	private static List<Bind> getBindAnnotations(Field field, Class<?> type,
			String value) {
		List<Bind> matchedBind = new ArrayList<Bind>();
		if (getNotBindAnnotation(field, type) == null) {
			Bind annotation = field.getAnnotation(Bind.class);
			if (annotation == null) {
				Annotation annotationBindings = field
						.getAnnotation(Bindings.class);
				if (annotationBindings != null) {
					for (Bind bind : ((Bindings) annotationBindings).value()) {
						if (bind.with().equals(type)) {
							String token = "";
							if (bind.value().contains(".")) {
								StringTokenizer tokenizer = new StringTokenizer(
										bind.value(), ".");
								token = tokenizer.nextToken();

							} else {
								token = bind.value();
							}
							if (value != null && token.equals(value)) {
								matchedBind.add(bind);
							} else if (value == null) {
								matchedBind.add(bind);
							}
						}
					}
				}
			} else if (annotation != null && annotation.with().equals(type)) {
				if (annotation.value().isEmpty() && annotation.inside()) {
					matchedBind.add(annotation);
				} else {
					String token = "";
					if (annotation.value().contains(".")) {
						StringTokenizer tokenizer = new StringTokenizer(
								annotation.value(), ".");
						token = tokenizer.nextToken();

					} else {
						token = annotation.value();
					}
					if (value != null && token.equals(value)) {
						matchedBind.add(annotation);
					} else if (value == null) {
						matchedBind.add(annotation);
					}
				}
			}
		}
		return matchedBind;
	}

	private static List<Bind> getBindAnnotations(Field field, Class<?> type) {
		return getBindAnnotations(field, type, null);
	}

	private static NotBind getNotBindAnnotation(Field field, Class<?> type) {
		NotBind matchedBind = null;
		NotBind annotation = field.getAnnotation(NotBind.class);

		if (annotation == null) {
			Annotation annotationBindings = field
					.getAnnotation(NotBindings.class);
			if (annotationBindings != null) {
				for (NotBind bind : ((NotBindings) annotationBindings).value()) {
					if (bind.with().equals(type)) {
						matchedBind = bind;
					}
				}
			}
		} else if (annotation != null && annotation.with().equals(type)) {
			matchedBind = annotation;
		}
		return matchedBind;
	}
}
