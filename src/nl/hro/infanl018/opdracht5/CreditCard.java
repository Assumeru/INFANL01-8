package nl.hro.infanl018.opdracht5;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CreditCard extends PaymentDetails {
	@Column(name = "bank")
	private String bank;

	public CreditCard() {
	}

	public CreditCard(String owner, String number, String bank) {
		super(owner, number);
		this.bank = bank;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}
}
