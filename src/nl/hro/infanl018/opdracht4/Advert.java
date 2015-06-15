package nl.hro.infanl018.opdracht4;

import java.util.Date;

public class Advert {
	private String name;
	private String description;
	private int startingPrice;
	private boolean active;
	private Date startingDate;
	private Category category;
	private User seller;
	private Offer successfulOffer;

	public Advert(String name, String description, int startingPrice, boolean active, Date startingDate, Category category, User seller, Offer successfulOffer) {
		this.name = name;
		this.description = description;
		this.startingPrice = startingPrice;
		this.active = active;
		this.startingDate = startingDate;
		this.category = category;
		this.seller = seller;
		this.successfulOffer = successfulOffer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(int startingPrice) {
		this.startingPrice = startingPrice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public Offer getSuccessfulOffer() {
		return successfulOffer;
	}

	public void setSuccessfulOffer(Offer successfulOffer) {
		this.successfulOffer = successfulOffer;
	}
}
