package io.projection.model.binding;

import io.projection.domain.Binding;

import java.util.ArrayList;
import java.util.List;


public class BindingDaoImpl implements BindingDao {
	private static List<Binding> bindingList;

	BindingDaoImpl() {
	}

	public void save(Binding binding) {
		bindingList.add(binding);
	}

	public Binding get(Class<?> originClass, Class<?> targetClass) {
		Binding matchedBinding = null;
		for (Binding binding : getBindingList()) {
			if ((binding.getOriginClass().equals(originClass) && binding
					.getTargetClass().equals(targetClass))
					|| (binding.getTargetClass().equals(originClass) && binding
							.getOriginClass().equals(targetClass))) {
				matchedBinding = binding;
				break;
			}
		}
		return matchedBinding;
	}

	private List<Binding> getBindingList() {
		if (bindingList == null) {
			bindingList = new ArrayList<Binding>();
		}
		return bindingList;
	}
}