package io.projection.samples.simplebinding;

import io.projection.mapping.EmptyMapping;
import io.projection.model.Mapping;

import java.util.Date;

public class Main extends EmptyMapping {

	public static void main(String[] args) {
		Mapping mapping = new EmptyMapping();
		
		Person origin = new Person("Name", "Last name", 27, new Date());
		System.out.println(origin.toString());
		
		Employee target = (Employee) mapping.map(origin, Employee.class);
		System.out.println(target.toString());
	}
}
