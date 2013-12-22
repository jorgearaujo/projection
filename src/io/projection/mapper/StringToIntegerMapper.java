package io.projection.mapper;

import io.projection.model.Mapper;

public class StringToIntegerMapper implements Mapper<String, Integer> {

	@Override
	public Integer map(String origin) throws Exception {
		return Integer.valueOf(origin);
	}

}
