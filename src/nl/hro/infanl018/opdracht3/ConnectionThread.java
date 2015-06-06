package nl.hro.infanl018.opdracht3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConnectionThread implements Runnable {
	private Connection conn;
	private int iterations = 0;
	private long sleep = 100;
	private long timeTaken = 0;

	public ConnectionThread(String url, String username, String password) throws SQLException {
		conn = DriverManager.getConnection(url, username, password);
		conn.setAutoCommit(false);
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	protected abstract void doSomething();

	@Override
	public void run() {
		for(int it = 0; it < iterations; it++) {
			long start = System.currentTimeMillis();
			doSomething();
			timeTaken += System.currentTimeMillis() - start;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected Connection getConnection() {
		return conn;
	}

	public double getAverageExecutionTime() {
		return timeTaken / (double)iterations;
	}
}
