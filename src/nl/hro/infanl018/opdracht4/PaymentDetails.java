package nl.hro.infanl018.opdracht4;

public abstract class PaymentDetails {
	private String owner;
	private String number;

	public PaymentDetails(String owner, String number) {
		super();
		this.owner = owner;
		this.number = number;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
