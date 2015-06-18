package nl.hro.infanl018.opdracht5;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;

public class Main {
	private SessionFactory sessionFactory;

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
		doSomething();
		sessionFactory.close();
	}

	private void doSomething() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Set<PaymentDetails> details = new HashSet<PaymentDetails>();
		details.add(new IDeal("voornaam", "", 12, 2015));
		details.add(new CreditCard("voornaam", "123", "bank"));
		User test = new User("voornaam", "achternaam", "email", "", details);
		session.save(test);
		session.getTransaction().commit();
		session.beginTransaction();
		User test2 = (User)session.get(User.class, 1);
		session.getTransaction().commit();
		session.close();
		System.out.println(test == test2);
		System.out.println(test.getPaymentDetails() == test2.getPaymentDetails());
	}
}
