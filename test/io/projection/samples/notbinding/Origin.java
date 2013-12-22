package io.projection.samples.notbinding;

import io.projection.lang.annotation.NotBind;

import java.util.Date;

public class Origin {

	private String name;
	private String lastname;
	@NotBind(with = Target.class)
	private Integer years;
	private Date date;

	public Origin(String name, String lastname, Integer years, Date date) {
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
