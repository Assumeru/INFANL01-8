package nl.hro.infanl018.opdracht2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<Integer, Long> failures = new HashMap<>();
	private Date start = new Date();

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
	}

	public void clearFailures() {
		failures.clear();
		start = new Date();
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

	public synchronized void report(int type, int delta) {
		synchronized(failures) {
			if(!failures.containsKey(type)) {
				failures.put(type, (long)0);
			}
			long f = failures.get(type);
			if(delta != 0) {
				f++;
				failures.put(type, f);
			}
			window.updateFailures(type, Math.round(f * 100000.0 / (new Date().getTime() - start.getTime())) / 100);
		}
		window.addPointToGraph(type, delta);
	}
}
