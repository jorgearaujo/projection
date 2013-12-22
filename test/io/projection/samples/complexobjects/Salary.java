package io.projection.samples.complexobjects;

public class Salary {

	private Double amount;
	private Integer numberOfPayments;
	
	public Salary() {
	}
	
	public Salary(Double amount, Integer numberOfPayments) {
		super();
		this.amount = amount;
		this.numberOfPayments = numberOfPayments;
	}

	public Integer getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(Integer numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Salary [amount=" + amount + ", numberOfPayments="
				+ numberOfPayments + "]";
	}

}
