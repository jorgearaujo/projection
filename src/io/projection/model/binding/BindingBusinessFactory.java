package io.projection.model.binding;

public class BindingBusinessFactory {

	private static BindingBusiness business;

	public static BindingBusiness getInstance() {
		if (business == null) {
			business = new BindingBusinessImpl();
		}
		return business;
	}

}
