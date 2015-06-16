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
	}

	private void doSomething() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.getTransaction().commit();
		session.close();
	}
}
