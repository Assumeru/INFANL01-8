package nl.hro.infanl018.opdracht5;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "offer")
public class Offer {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@Column
	private int price;
	@Column
	private Date date;
	@Column
	private Advert advert;
	@Column
	private User bidder;

	public Offer(int id, int price, Date date, Advert advert, User bidder) {
		this.id = id;
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
