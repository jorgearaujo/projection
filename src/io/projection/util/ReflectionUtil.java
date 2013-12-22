package io.projection.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionUtil {

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

	public static Boolean existsField(Class<?> classToCheck, String field) {
		Boolean exist = Boolean.TRUE;
		try {
			if (classToCheck.getDeclaredField(field) == null) {
				exist = Boolean.FALSE;
			}
		} catch (NoSuchFieldException e) {
			exist = Boolean.FALSE;
		} catch (SecurityException e) {
			exist = Boolean.FALSE;
		}
		return exist;
	}

	public static Boolean existsField(Class<?> classToCheck, String field,
			String separator) {
		Boolean exist = Boolean.TRUE;
		try {
			if (field.contains(separator)) {
				StringTokenizer tokenizer = new StringTokenizer(field,
						separator);
				Class<?> currentClass = classToCheck;
				int numTokens = tokenizer.countTokens();
				while (numTokens > 0) {
					String token = tokenizer.nextToken();
					if (existsField(currentClass, token)) {
						currentClass = currentClass.getDeclaredField(token)
								.getType();
					} else {
						exist = Boolean.FALSE;
						break;
					}
					numTokens--;
				}
			} else {
				exist = existsField(classToCheck, field);
			}
		} catch (NoSuchFieldException e) {
			exist = Boolean.FALSE;
		} catch (SecurityException e) {
			exist = Boolean.FALSE;
		}
		return exist;
	}

	public static Object fieldGet(Field field, Object object) {
		Object returnObject = null;
		Boolean fieldAccessible = makeAccesible(field);
		try {
			returnObject = field.get(object);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		if (!fieldAccessible) {
			makeNoAccesible(field);
		}
		return returnObject;
	}

	public static void fieldSet(Field field, Object instance,
			Object objectToBeSet) {
		Boolean fieldAccessible = makeAccesible(field);
		try {
			field.set(instance, objectToBeSet);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		if (!fieldAccessible) {
			makeNoAccesible(field);
		}
	}

	public static Boolean makeAccesible(Field field) {
		Boolean fieldAccessible = field.isAccessible();
		if (!fieldAccessible) {
			field.setAccessible(true);
		}
		return fieldAccessible;
	}

	public static void makeNoAccesible(Field field) {
		field.setAccessible(false);
	}

	public static Field getDeclaredField(Class<?> cl, String fieldName) {
		Field field = null;
		try {
			field = cl.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		}
		return field;
	}

	public static Object instantiate(Class<?> classToInstantiate) {
		Object returnObject = null;
		try {
			returnObject = classToInstantiate.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return returnObject;
	}

	@SuppressWarnings("rawtypes")
	public static List<Class> getClassesForPackage(String pkgname) {
		// String pkgname = pkg.getName();

		List<Class> classes = new ArrayList<Class>();

		// Get a File object for the package
		File directory = null;
		String fullPath;
		String relPath = pkgname.replace('.', '/');

		// System.out.println("ClassDiscovery: Package: " + pkgname +
		// " becomes Path:" + relPath);

		URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);

		// System.out.println("ClassDiscovery: Resource = " + resource);
		if (resource == null) {
			throw new RuntimeException("No resource for " + relPath);
		}
		fullPath = resource.getFile();
		// System.out.println("ClassDiscovery: FullPath = " + resource);

		try {
			directory = new File(resource.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(
					pkgname
							+ " ("
							+ resource
							+ ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...",
					e);
		} catch (IllegalArgumentException e) {
			directory = null;
		}
		// System.out.println("ClassDiscovery: Directory = " + directory);

		if (directory != null && directory.exists()) {

			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {

				// we are only interested in .class files
				if (files[i].endsWith(".class")) {

					// removes the .class extension
					String className = pkgname + '.'
							+ files[i].substring(0, files[i].length() - 6);

					// System.out.println("ClassDiscovery: className = " +
					// className);

					try {
						classes.add(Class.forName(className));
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(
								"ClassNotFoundException loading " + className);
					}
				}
			}
		} else {
			try {
				String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar")
						.replaceFirst("file:", "");
				@SuppressWarnings("resource")
				JarFile jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if (entryName.startsWith(relPath)
							&& entryName.length() > (relPath.length() + "/"
									.length())) {

						// System.out.println("ClassDiscovery: JarEntry: " +
						// entryName);
						String className = entryName.replace('/', '.')
								.replace('\\', '.').replace(".class", "");

						// System.out.println("ClassDiscovery: className = " +
						// className);
						try {
							classes.add(Class.forName(className));
						} catch (ClassNotFoundException e) {
							throw new RuntimeException(
									"ClassNotFoundException loading "
											+ className);
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(pkgname + " (" + directory
						+ ") does not appear to be a valid package", e);
			}
		}
		return classes;
	}

	public static boolean isBoxedPrimitive(Class<?> fieldClass) {
		if (fieldClass.equals(Byte.class) || fieldClass.equals(Short.class)
				|| fieldClass.equals(Integer.class)
				|| fieldClass.equals(Long.class)
				|| fieldClass.equals(Float.class)
				|| fieldClass.equals(Double.class)
				|| fieldClass.equals(Boolean.class)
				|| fieldClass.equals(Character.class)
				|| fieldClass.equals(String.class)
				|| fieldClass.equals(List.class)
				|| fieldClass.equals(Set.class)
				|| fieldClass.equals(Date.class) || fieldClass.isArray()) {
			return true;
		}
		return false;

	}

}
