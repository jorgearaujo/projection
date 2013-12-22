package io.projection.samples.complexobjects;

public class Employee {

	private String name;
	private String lastname;
	private Integer years;
	private Salary salary;

	public String getEmployeeName() {
		return name;
	}

	public void setEmployeeName(String employeeName) {
		this.name = employeeName;
	}

	public String getEmployeeLastname() {
		return lastname;
	}

	public void setEmployeeLastname(String employeeLastname) {
		this.lastname = employeeLastname;
	}

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [employeeName=" + name + ", employeeLastname="
				+ lastname + ", years=" + years + ", salary=" + salary + "]";
	}
}
