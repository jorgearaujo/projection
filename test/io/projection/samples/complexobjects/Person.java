package io.projection.samples.complexobjects;

import java.util.Date;

public class Person {

	private String name;
	private String lastname;
	private Integer years;
	private Date date;
	private Salary salary;

	public Person(String name, String lastname, Integer years, Date date,
			Salary salary) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.years = years;
		this.date = date;
		this.salary = salary;
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

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", lastname=" + lastname + ", years="
				+ years + ", date=" + date + ", salary=" + salary + "]";
	}

}
