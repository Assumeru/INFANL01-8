package nl.hro.infanl018.opdracht2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;

public class Corrupter implements Runnable {
	private static final int SLEEP = 500;

	private boolean running = true;
	private ThreadManager manager;
	private Connection conn;

	public Corrupter(ThreadManager manager) throws SQLException {
		this.manager = manager;
		conn = DriverManager.getConnection(manager.getUrl(), manager.getUsername(), manager.getPassword());
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		conn.setAutoCommit(false);
	}

	@Override
	public void run() {
		while(running) {
			try {
				dirtyRead();
				unrepeatableRead();
				phantomRead();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			deadLock();
			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				System.err.println("Ik lag te slapen, maar je moest me weer wakker maken hè?");
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deadLock() {
		try {
			insertProduct("Kaas");
			deleteProduct("Kaas");
			report(ThreadManager.DEAD_LOCK, 0);
		} catch (SQLTransactionRollbackException e) {
			report(ThreadManager.DEAD_LOCK, 10);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertProduct(String product) throws SQLException {
		PreparedStatement p = conn.prepareStatement("INSERT IGNORE INTO producten VALUES (?, ?)");
		p.setString(1, product);
		p.setInt(2, 0);
		p.execute();
	}

	private void deleteProduct(String product) throws SQLException {
		PreparedStatement p = conn.prepareStatement("DELETE FROM producten WHERE naam = ?");
		p.setString(1, product);
		p.execute();
	}

	private void dirtyRead() throws SQLException {
		PreparedStatement p;
		if(Math.random() < 0.5) {
			// In 50% van de gevallen update update het aantal producten aan de
			// hand van de veranderingen tabel
			int sum = 0;
			p = conn.prepareStatement("SELECT SUM(verandering) FROM veranderingen WHERE product = ?");
			p.setString(1, "Mac Book");
			ResultSet r = p.executeQuery();
			if(r.next()) {
				sum = r.getInt(1);
				report(ThreadManager.DIRTY_READ, sum);
			}
			p = conn.prepareStatement("UPDATE producten SET aantal = ? WHERE naam = ?");
			p.setInt(1, sum);
			p.setString(2, "Mac Book");
			p.execute();
			conn.commit();
		} else {
			// In de andere 50% van de gevallen insert een verandering en commit
			// deze niet
			p = conn.prepareStatement("INSERT INTO veranderingen VALUES (?, ?)");
			p.setString(1, "Mac Book");
			p.setInt(2, 10);
			p.execute();
			conn.rollback();
			report(ThreadManager.DIRTY_READ, 0);
		}
	}

	private int selectNumProducts(String product) throws SQLException {
		int out = 0;
		PreparedStatement p = conn.prepareStatement("SELECT aantal FROM producten WHERE naam = ?");
		p.setString(1, product);
		ResultSet r = p.executeQuery();
		while(r.next()) {
			out = r.getInt(1);
		}
		r.close();
		return out;
	}

	private void unrepeatableRead() throws SQLException {
		// unrepeatableRead: A non-repeatable read occurs, when during the
		// course of a transaction,
		// a row is retrieved twice and the values within the row differ between
		// reads.
		// UserA reads
		// UserB reads
		// UserB runs transaction and commits
		// UserA reads - Value is nu anders dan de eerste keer
		int read1 = selectNumProducts("Mac Book");
		int read2 = selectNumProducts("Mac Book");
		conn.commit();
		report(ThreadManager.NON_REPEATABLE_READ, read1 - read2);
	}

	private int numChanges(String product) throws SQLException {
		int numChanges = 0;
		PreparedStatement p = conn.prepareStatement("SELECT COUNT(1) FROM veranderingen WHERE product = ?");
		p.setString(1, product);
		ResultSet r = p.executeQuery();
		while(r.next()) {
			numChanges = r.getInt(1);
		}
		r.close();
		return numChanges;
	}

	private void phantomRead() throws SQLException {
		// phantomread maken: A phantom read occurs when, in the course of a
		// transaction,
		// two identical queries are executed, and the collection of
		// rows returned by the second query is different from the first.
		int read1 = numChanges("Mac Book");
		int read2 = numChanges("Mac Book");
		conn.commit();
		report(ThreadManager.PHANTOM_READ, read1 - read2);
	}

	public void cancel() {
		running = false;
	}

	private void report(int type, int delta) {
		manager.report(type, delta);
	}
}
