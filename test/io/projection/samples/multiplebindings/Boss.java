package io.projection.samples.multiplebindings;

public class Boss {

	private String bossName;
	private String bossLastname;
	private Integer years;
	private Double salary;

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	public String getBossLastname() {
		return bossLastname;
	}

	public void setBossLastname(String bossLastname) {
		this.bossLastname = bossLastname;
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
		return "Boss [bossName=" + bossName + ", bossLastname=" + bossLastname
				+ ", years=" + years + ", salary=" + salary + "]";
	}

}
