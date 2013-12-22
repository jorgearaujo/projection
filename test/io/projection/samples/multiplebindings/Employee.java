package io.projection.samples.multiplebindings;

import io.projection.lang.annotation.Bind;


public class Employee {

	private String employeeName;
	@Bind(with=Person.class, value="lastname")
	private String employeeLastname;
	private Integer years;
	private Double salary;

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeLastname() {
		return employeeLastname;
	}

	public void setEmployeeLastname(String employeeLastname) {
		this.employeeLastname = employeeLastname;
	}

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [employeeName=" + employeeName + ", employeeLastname="
				+ employeeLastname + ", years=" + years + ", salary=" + salary
				+ "]";
	}

}
