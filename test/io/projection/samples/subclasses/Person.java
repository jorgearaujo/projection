package io.projection.samples.subclasses;

import io.projection.lang.annotation.Bind;

import java.util.Date;

public class Person {

	private String name;
	private String lastname;
	private Integer years;
	private Date date;
	@Bind(with=Employee.class, value="salary.amount")
	private Double amountMoney;
	@Bind(with=Employee.class, value="salary.numberOfPayments")
	private Integer paymentsNumber;

	public Person() {
	}
	
	public Person(String name, String lastname, Integer years, Date date,
			Double amountMoney, Integer paymentsNumber) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.years = years;
		this.date = date;
		this.amountMoney = amountMoney;
		this.paymentsNumber = paymentsNumber;
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

	public Double getAmountMoney() {
		return amountMoney;
	}

	public void setAmountMoney(Double amountMoney) {
		this.amountMoney = amountMoney;
	}

	public Integer getPaymentsNumber() {
		return paymentsNumber;
	}

	public void setPaymentsNumber(Integer paymentsNumber) {
		this.paymentsNumber = paymentsNumber;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", lastname=" + lastname + ", years="
				+ years + ", date=" + date + ", amountMoney=" + amountMoney
				+ ", paymentsNumber=" + paymentsNumber + "]";
	}

}
