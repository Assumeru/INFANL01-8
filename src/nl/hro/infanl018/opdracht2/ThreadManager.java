package nl.hro.infanl018.opdracht2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
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

	public void report(String product, int quant1, int quant2) {
		if(quant1 != quant2) {
			System.out.println(product);
		}
		window.addPointToGraph(quant1 - quant2);
	}
}
