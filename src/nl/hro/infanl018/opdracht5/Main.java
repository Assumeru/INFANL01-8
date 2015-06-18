package nl.hro.infanl018.opdracht5;

import java.util.Date;
import java.util.HashSet;
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
		addUser();
		sessionFactory.close();
	}

	private void doSomething() {
	}

	public void addUser() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Set<PaymentDetails> detailsArmindo = new HashSet<PaymentDetails>();
		detailsArmindo.add(new CreditCard("Armindo", "7654321", 12, 2015));
		detailsArmindo.add(new IDeal("Armindo", "1234567", "ING Bank"));
		armindo = new User("Armindo", "Maurits", "0882940@hr.nl", "testpassword", detailsArmindo);

		Set<PaymentDetails> detailsWim = new HashSet<PaymentDetails>();
		detailsWim.add(new CreditCard("Wim", "6452100", 12, 2014));
		detailsWim.add(new IDeal("Wim", "0012546", "ING Bank"));
		wim = new User("Wim", "Procee", "iets@hr.nl", "testpassword", detailsArmindo);

		session.save(armindo);
		session.save(wim);
		session.getTransaction().commit();
		session.close();
	}

	public void addAdvert() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		//vul de laatste dingen in, categorie etc. thx
		Advert advert = new Advert("Witte auto te koop", "Hele mooie witte auto te koop. koop dit nu", 1500, true, new Date(), null, );
		session.save(advert);
		session.getTransaction().commit();
		session.close();
	}

	public void addCategory() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		categoryAuto = new Category(null , "Auto's");
		session.save(categoryAuto);
		session.getTransaction().commit();

		categoryWitteAuto = new Category(categoryAuto, "Witte auto's");
		session.save(categoryWitteAuto);
		session.getTransaction().commit();
		session.close();
	}
}
