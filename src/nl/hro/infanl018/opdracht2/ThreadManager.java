package nl.hro.infanl018.opdracht2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
	public static final int DIRTY_READ = 0;
	public static final int NON_REPEATABLE_READ = 1;
	public static final int PHANTOM_READ = 2;
	public static final int DEAD_LOCK = 3;

	private List<Corrupter> threads = new ArrayList<>();
	private int numThreads;
	private String url;
	private String username;
	private String password;
	private Main window;

	public ThreadManager(Main window, String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.window = window;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
		updateThreads();
		Corrupter.clearFailures();
	}

	private void updateThreads() {
		if(threads.size() != numThreads) {
			try {
				while(threads.size() < numThreads) {
					Corrupter c = new Corrupter(this);
					new Thread(c).start();
					threads.add(c);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			while(threads.size() > numThreads) {
				Corrupter c = threads.remove(threads.size() - 1);
				c.cancel();
			}
		}
	}

	public synchronized void report(int type, int delta, double failuresPerSecond) {
		if(delta != 0) {
			//System.out.println(type+" "+delta);
		}
		window.addPointToGraph(type, delta);
		window.updateFailures(type, Math.round(failuresPerSecond * 100) / 100);
	}
}
