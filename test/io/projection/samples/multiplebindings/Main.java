package io.projection.samples.multiplebindings;

import io.projection.mapping.EmptyMapping;
import io.projection.model.Mapping;

import java.util.Date;

public class Main extends EmptyMapping {

	public static void main(String[] args) {
		Mapping mapping = new EmptyMapping();
		
		Person person = new Person("Name", "Last name", 27, new Date());
		System.out.println(person.toString());
		
		Employee employee = (Employee) mapping.map(person, Employee.class);
		System.out.println(employee.toString());
		
		Boss boss = (Boss) mapping.map(person, Boss.class);
		System.out.println(boss.toString());
	}
}
