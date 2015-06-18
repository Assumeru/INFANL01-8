package nl.hro.infanl018.opdracht5;

import javax.persistence.Column;
import javax.persistence.Entity;
@Entity
public class CreditCard extends PaymentDetails {
	@Column(name = "expmonth")
	private int expirationMonth;
	@Column(name = "expyear")
	private int expirationYear;

	public CreditCard() {
	}

	public CreditCard(String owner, String number, int expirationMonth, int expirationYear) {
		super(owner, number);
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
	}

	public int getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public int getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(int expirationYear) {
		this.expirationYear = expirationYear;
	}
}