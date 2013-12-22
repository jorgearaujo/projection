package io.projection.mapper;

import io.projection.model.Mapper;

public class IntegerToStringMapper implements Mapper<Integer, String> {

	@Override
	public String map(Integer origin) throws Exception {
		return String.valueOf(origin);
	}

}
