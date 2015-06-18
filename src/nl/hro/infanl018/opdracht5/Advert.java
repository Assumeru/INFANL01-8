package nl.hro.infanl018.opdracht5;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "adverts")
public class Advert {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@Column
	private String name;
	@Column
	private String description;
	@Column
	private int startingPrice;
	@Column
	private boolean active;
	@Column
	private Date startingDate;
	@OneToMany
	private Set<Category> categories;
	@ManyToOne
	private User seller;
	@ManyToOne
	private Offer successfulOffer;

	public Advert() {
	}

	public Advert(String name, String description, int startingPrice, boolean active, Date startingDate, Set<Category> categories, User seller, Offer successfulOffer) {
		this.name = name;
		this.description = description;
		this.startingPrice = startingPrice;
		this.active = active;
		this.startingDate = startingDate;
		this.categories = categories;
		this.seller = seller;
		this.successfulOffer = successfulOffer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
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
