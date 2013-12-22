package io.projection.mapping;

public class SimpleMapping extends EmptyMapping {

	public SimpleMapping() {
		super();
		setPackage("io.projection.mapper");
	}

	@Override
	public Boolean isCacheEnabled() {
		return super.isCacheEnabled();
	}

}
