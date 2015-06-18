package nl.hro.infanl018.opdracht5;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;

public class Main {
	private User armindo;
	private User wim;
	private SessionFactory sessionFactory;
	private Category categoryAuto;
	private Category categoryWitteAuto;
	private Advert advertAuto;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch(JDBCConnectionException e) {
			System.out.println("Failed to connect.");
			System.exit(1);
		}
		addUsers();
		addCategories();
		addAdverts();
		addOffers();
		setHighestOffer();
		addResponses();
		sessionFactory.close();
	}

	private void addUsers() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Set<PaymentDetails> detailsArmindo = new HashSet<PaymentDetails>();
		detailsArmindo.add(new CreditCard("Armindo", "7654321", 12, 2015));
		detailsArmindo.add(new IDeal("Armindo", "1234567", "ING Bank"));
		armindo = new User("Armindo", "Maurits", "0882940@hr.nl", "testpassword", detailsArmindo);

		Set<PaymentDetails> detailsWim = new HashSet<PaymentDetails>();
		detailsWim.add(new CreditCard("Wim", "6452100", 12, 2014));
		detailsWim.add(new IDeal("Wim", "0012546", "ING Bank"));
		wim = new User("Wim", "Procee", "iets@hr.nl", "testpassword", detailsWim);

		session.save(armindo);
		session.save(wim);
		session.getTransaction().commit();
		session.close();
	}

	private void addAdverts() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Set<Category> categories = new HashSet<Category>();
		categories.add(categoryAuto);
		categories.add(categoryWitteAuto);
		advertAuto = new Advert("Witte auto te koop", "Hele mooie witte auto te koop. koop dit nu", 1500, true, new Date(), categories, armindo, null);
		session.save(advertAuto);
		session.getTransaction().commit();
		session.close();
	}

	private void addCategories() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		categoryAuto = new Category(null , "Auto's");
		session.save(categoryAuto);

		categoryWitteAuto = new Category(categoryAuto, "Witte auto's");
		session.save(categoryWitteAuto);
		session.getTransaction().commit();
		session.close();
	}

	private void addOffers() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Offer o1 = new Offer(1, new Date(), advertAuto, armindo);
		Offer o2 = new Offer(20, new Date(), advertAuto, wim);
		session.save(o1);
		session.save(o2);
		session.getTransaction().commit();
		session.close();
	}

	private void setHighestOffer() {
		Session session = sessionFactory.openSession();

		List<Offer> offers = session.createCriteria(Offer.class).list();
		Offer highest = null;
		for(Offer o : offers) {
			if(highest == null || highest.getPrice() < o.getPrice()) {
				highest = o;
			}
		}
		System.out.println(highest.getPrice()+" offers: "+offers.size());

		session.beginTransaction();

		advertAuto.setSuccessfulOffer(highest);
		session.save(advertAuto);

		session.getTransaction().commit();
		session.close();
	}

	private void addResponses() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		AdvertResponse r1 = new AdvertResponse("Hij is heel mooi", new Date(), armindo, advertAuto);
		AdvertResponse r2 = new AdvertResponse("Hij is helemaal niet mooi", new Date(), wim, advertAuto);

		session.save(r1);
		session.save(r2);

		session.getTransaction().commit();
		session.close();
	}
}
