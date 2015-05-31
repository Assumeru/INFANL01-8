package nl.hro.infanl018.opdracht2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Corrupter implements Runnable {
	private static final int SLEEP = 1000;

	private boolean running = true;
	private ThreadManager manager;
	private Connection conn;

	public Corrupter(ThreadManager manager) throws SQLException {
		this.manager = manager;
		conn = DriverManager.getConnection(manager.getUrl(), manager.getUsername(), manager.getPassword());
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	}

	@Override
	public void run() {
		while(running) {
			try {
				addChange();
				checkRead();
                unrepeatableRead();
                phantomRead();
			} catch (SQLException e1) {
				System.err.println("Kon niet lezen/schrijven, sorry.");
				e1.printStackTrace();
			}
			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				System.err.println("Ik lag te slapen, maar je moest me weer wakker maken hè?");
			}
		}
	}

	private void addChange() throws SQLException {
		PreparedStatement p = conn.prepareStatement("INSERT INTO veranderingen VALUES (?, ?)");
		p.setString(1, "Mac Book");
		p.setInt(2, (int)Math.round(Math.random()*20)-10);
		p.execute();
		p = conn.prepareStatement("UPDATE producten SET aantal = (SELECT SUM(verandering) FROM veranderingen WHERE product  = ?) WHERE naam = ?");
		p.setString(1, "Mac Book");
		p.setString(2, "Mac Book");
		p.execute();
	}

	private void checkRead() throws SQLException {
		PreparedStatement p = conn.prepareStatement("SELECT producten.naam, producten.aantal, j.hoeveelheid "
				+ "FROM producten, (SELECT product, SUM(verandering) AS hoeveelheid FROM veranderingen) AS j "
				+ "WHERE producten.naam = j.product");
		ResultSet r = p.executeQuery();
		while(r.next()) {
			String product = r.getString(1);
			int aantal = r.getInt(2);
			int hoeveelheid = r.getInt(3);
			manager.report(product, aantal, hoeveelheid);
		}
		r.close();
	}

	public void unrepeatableRead() throws SQLException {
        //unrepeatableRead: A non-repeatable read occurs, when during the course of a transaction,
        //a row is retrieved twice and the values within the row differ between reads.
        //UserA reads
        //UserB reads
        //UserB runs transaction and commits
        //UserA reads - Value is nu anders dan de eerste keer
        PreparedStatement p = conn.prepareStatement("SELECT producten.naam, producten.aantal FROM producten");
        ResultSet r = p.executeQuery();
        while(r.next()) {
            String product = r.getString(1);
            int aantal = r.getInt(2);
            System.out.println("unrepeatableRead:" + product + aantal + "");
        }
        r.close();

        PreparedStatement ps = conn.prepareStatement("UPDATE producten SET producten.aantal = ? WHERE producten.naam = ?");
        ps.setInt(1, (int)Math.round(Math.random()*20)-10);
        ps.setString(2, "Mac Book");

        ps.execute();
    }

    public void phantomRead() throws SQLException {
        // phantomread maken: A phantom read occurs when, in the course of a transaction,
        // two identical queries are executed, and the collection of
        // rows returned by the second query is different from the first.
        PreparedStatement p = conn.prepareStatement("SELECT producten.naam, producten.aantal FROM producten");
        ResultSet r = p.executeQuery();
        while(r.next()) {
            String product = r.getString(1);
            int aantal = r.getInt(2);
            System.out.println("phantomRead:" + product + aantal + "");
        }
        r.close();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO veranderingen VALUES (?, ?)");
        ps.setString(1, "Mac Book");
        ps.setInt(2, (int)Math.round(Math.random()*20)-10);
        ps.execute();
    }

    public void deadlock() throws SQLException {


    }

	public void cancel() {
		running = false;
	}
}
