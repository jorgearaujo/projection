package io.projection.model.binding;

public class BindingDaoFactory {

	private static BindingDao dao;
	
	public static BindingDao getInstance() {
		if (dao == null) {
			dao = new BindingDaoImpl();
		}
		return dao;
	}
	
}
