package nl.hro.infanl018.opdracht3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConnectionThread implements Runnable {
	private Connection conn;
	private int iterations = 0;
	private long sleep = 100;
	private long timeTaken = 0;
	private Main parent;

	public ConnectionThread(Main main) throws SQLException {
		conn = DriverManager.getConnection(main.getUrl(), main.getUsername(), main.getPassword());
		conn.setAutoCommit(false);
		parent = main;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	protected abstract void doSomething() throws SQLException;

	@Override
	public void run() {
		try {
			for(int it = 0; it < iterations; it++) {
				long start = System.currentTimeMillis();
				doSomething();
				timeTaken += System.currentTimeMillis() - start;
				Thread.sleep(sleep);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		parent.report(this);
	}

	protected Connection getConnection() {
		return conn;
	}

	public double getAverageExecutionTime() {
		return timeTaken / (double)iterations;
	}
}
