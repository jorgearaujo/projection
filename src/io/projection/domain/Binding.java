package io.projection.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Specify the mapping relation between two classes, on both sides. It is needed
 * to be passed to the mapping business logic to know how to map. It is created
 * by the binding business logic, before the mapping, and can be saved if cache
 * is enabled.
 * 
 * @author Jorge Araújo
 * 
 */
public class Binding {

	private Class<?> originClass; // PK
	private Class<?> targetClass; // PK
	private List<Partnership> partnerships;

	public Binding() {
		partnerships = new ArrayList<Partnership>();
	}

	public Class<?> getOriginClass() {
		return originClass;
	}

	public void setOriginClass(Class<?> originClass) {
		this.originClass = originClass;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public List<Partnership> getPartnerships() {
		return partnerships;
	}

	public void setPartnerships(List<Partnership> partnerships) {
		this.partnerships = partnerships;
	}

	@Override
	public String toString() {
		return "Binding [originClass=" + originClass + ", targetClass="
				+ targetClass + ", partnerships=" + partnerships + "]";
	}

}