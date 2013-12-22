package io.projection.util;

import io.projection.lang.annotation.Bind;
import io.projection.lang.annotation.Bindings;
import io.projection.lang.annotation.NotBind;
import io.projection.lang.annotation.NotBindings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class MapperUtil {

	public static List<Field> getAllFields(Class<?> type) {
		List<Field> fields = new ArrayList<Field>();
		for (Field field : type.getDeclaredFields()) {
			fields.add(field);
		}
		if (type.getSuperclass() != null) {
			fields.addAll(getAllFields(type.getSuperclass()));
		}
		return fields;
	}

	public static List<Bind> getBindAnnotations(Field field, Class<?> type,
			String value) {
		List<Bind> matchedBind = new ArrayList<Bind>();
		Bind annotation = field.getAnnotation(Bind.class);
		if (annotation == null) {
			Annotation annotationBindings = field.getAnnotation(Bindings.class);
			if (annotationBindings != null) {
				for (Bind bind : ((Bindings) annotationBindings).value()) {
					if (bind.with().equals(type)) {
						StringTokenizer tokenizer = new StringTokenizer(
								bind.value(), ".");
						String token = tokenizer.nextToken();
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
				StringTokenizer tokenizer = new StringTokenizer(
						annotation.value(), ".");
				String token = tokenizer.nextToken();
				if (value != null && token.equals(value)) {
					matchedBind.add(annotation);
				} else if (value == null) {
					matchedBind.add(annotation);
				}
			}
		}
		return matchedBind;
	}

	public static List<Bind> getBindAnnotations(Field field, Class<?> type) {
		return MapperUtil.getBindAnnotations(field, type, null);
	}

	public static Bind getFirstBindAnnotation(Field field, Class<?> type,
			String value) {
		Bind returnBind = null;
		List<Bind> bindList = MapperUtil.getBindAnnotations(field, type, value);
		if (!bindList.isEmpty()) {
			returnBind = bindList.get(0);
		}
		return returnBind;
	}

	public static Bind getFirstBindAnnotation(Field field, Class<?> type) {
		return MapperUtil.getFirstBindAnnotation(field, type, null);
	}

	public static NotBind getNotBindAnnotation(Field field, Class<?> type) {
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

	public static Field getBoundField(Object origin, Class<?> originClass,
			Class<?> targetClass, Field targetField)
			throws NoSuchFieldException, SecurityException {
		// Se busca el bind del atributo respecto al origen
		List<Bind> targetBind = MapperUtil.getBindAnnotations(targetField,
				originClass);

		Field originField = null;
		// Si hay anotación, se busca en la clase origen el valor asociado
		if (!targetBind.isEmpty()) {
			originField = originClass.getField(targetBind.get(0).value());
		} else {
			// Se busca el bind en la clase origen
			List<Field> originFields = getAllFields(originClass);
			for (Field field : originFields) {
				List<Bind> originBind = MapperUtil.getBindAnnotations(field,
						targetClass);
				if (!originBind.isEmpty()
						&& originBind.get(0).value()
								.equals(targetField.getName())) {
					originField = field;
					break;
				}
			}
			if (originField == null) {
				// Si todavía no hay anotación, se tira de nombre
				originField = originClass.getField(targetField.getName());
			}
		}

		return originField;
	}



}
