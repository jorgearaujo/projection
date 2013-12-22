package io.projection.model;

public interface Mapper<FROM, TO> {

	/**
	 * Maps an object of type specified by FROM to an object of type TO.
	 * 
	 * @param origin Object to be mapped with.
	 * @return Mapped object.
	 * @throws Exception
	 */
	public TO map(FROM origin) throws Exception;

}
