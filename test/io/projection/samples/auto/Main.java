package io.projection.samples.auto;

import io.projection.mapping.EmptyMapping;
import io.projection.model.Mapping;

import java.util.Date;

public class Main extends EmptyMapping {

	public static void main(String[] args) {
		Mapping mapping = new EmptyMapping();
		Origin origin = new Origin("Name", "Last name", 27, new Date());
		System.out.println(origin.toString());
		Target target = (Target) mapping.map(origin, Target.class);
		System.out.println(target.toString());
	}
}
