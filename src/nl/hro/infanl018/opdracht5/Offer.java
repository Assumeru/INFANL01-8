package nl.hro.infanl018.opdracht5;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "offers")
public class Offer {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@Column
	private int price;
	@Column
	private Date date;
	@OneToOne
	private Advert advert;
	@OneToOne
	private User bidder;

	public Offer() {
	}

	public Offer(int price, Date date, Advert advert, User bidder) {
		this.price = price;
		this.date = date;
		this.advert = advert;
		this.bidder = bidder;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
