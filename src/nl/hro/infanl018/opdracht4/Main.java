package nl.hro.infanl018.opdracht4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
	private SessionFactory sessionFactory;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		doSomething();
		sessionFactory.close();
	}

	private void doSomething() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		User test = new User("voornaam", "achternaam", "email", "", null);
		session.save(test);
		session.getTransaction().commit();
		session.close();
	}
}
