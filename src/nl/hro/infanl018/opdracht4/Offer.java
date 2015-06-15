package nl.hro.infanl018.opdracht4;

import java.util.Date;

public class Offer {
	private int price;
	private Date date;
	private Advert advert;
	private User bidder;

	public Offer(int price, Date date, Advert advert, User bidder) {
		this.price = price;
		this.date = date;
		this.advert = advert;
		this.bidder = bidder;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Advert getAdvert() {
		return advert;
	}

	public void setAdvert(Advert advert) {
		this.advert = advert;
	}

	public User getBidder() {
		return bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}
}
