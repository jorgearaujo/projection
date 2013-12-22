package io.projection.samples.subclasses;

public class Salary {

	// @Bind(with = Person.class, value = "amountMoney")
	private Double amount;
	// @Bind(with = Person.class, value = "paymentsNumber")
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
