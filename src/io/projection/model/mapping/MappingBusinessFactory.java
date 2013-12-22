package io.projection.model.mapping;

import io.projection.model.Mapping;

public class MappingBusinessFactory {

	private static MappingBusiness business;

	public static MappingBusiness getInstance(Mapping mapping) {
		if (business == null) {
			business = new MappingBusinessImpl(mapping);
		}
		return business;
	}

}
