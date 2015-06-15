package nl.hro.infanl018.opdracht4;

public class CreditCard extends PaymentDetails {
	private String bank;

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
