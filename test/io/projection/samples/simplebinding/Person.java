package io.projection.samples.simplebinding;

import io.projection.lang.annotation.Bind;

import java.util.Date;

public class Person {

	@Bind(with=Employee.class, value="employeeName")
	private String name;
	private String lastname;
	private Integer years;
	private Date date;

	public Person(String name, String lastname, Integer years, Date date) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.years = years;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Origin [name=" + name + ", lastname=" + lastname + ", years="
				+ years + ", date=" + date + "]";
	}

}
