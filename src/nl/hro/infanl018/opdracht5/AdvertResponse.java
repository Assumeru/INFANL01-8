package nl.hro.infanl018.opdracht5;

import java.util.Date;

public class AdvertResponse {
	private String text;
	private Date date;
	private User user;
	private Advert advert;

	public AdvertResponse(String text, Date date, User user, Advert advert) {
		this.text = text;
		this.date = date;
		this.user = user;
		this.advert = advert;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Advert getAdvert() {
		return advert;
	}

	public void setAdvert(Advert advert) {
		this.advert = advert;
	}
}
