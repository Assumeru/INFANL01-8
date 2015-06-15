package nl.hro.infanl018.opdracht4;

public class IDeal extends PaymentDetails {
	private int expirationMonth;
	private int expirationYear;

	public IDeal(String owner, String number, int expirationMonth, int expirationYear) {
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
