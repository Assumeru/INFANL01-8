package nl.hro.infanl018.opdracht3;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main {
	private static final int NUM_INSERTS = 3;
	private static final int NUM_SELECTS = 2;
	private String url;
	private String username;
	private String password;
	private List<Double> insertTimes = new ArrayList<>();
	private List<Double> selectTimes = new ArrayList<>();


	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Usage: URL USERNAME PASSWORD");
			System.exit(1);
		}
		new Main(args[0], args[1], args[2]);
	}

	public Main(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
		runInserts(NUM_INSERTS, 600);
		runSelects(NUM_SELECTS, 600);
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

	private double getAvg(List<Double> list) {
		double sum = 0;
		for(Double d : list) {
			sum += d;
		}
		return sum / (list.size() * 1000);
	}

	public synchronized void report(ConnectionThread thread) {
		if(thread instanceof InsertThread) {
			insertTimes.add(thread.getAverageExecutionTime());
			if(insertTimes.size() == NUM_INSERTS) {
				System.out.println("Gemiddelde insert tijd: "+getAvg(insertTimes)+"s");
			}
		}else if((thread instanceof SelectThread)) {
			selectTimes.add(thread.getAverageExecutionTime());
			if(selectTimes.size() == NUM_SELECTS) {
				System.out.println("Gemiddelde select tijd: "+getAvg(selectTimes)+"s");
			}
		}
	}

	private void runInserts(int threads, int it) {
		try {
			for(int i = 0; i < threads; i++) {
				InsertThread runnable = new InsertThread(this);
				runnable.setIterations(it);
				new Thread(runnable).start();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void runSelects(int threads, int it) {
		try {
			for(int i = 0; i < threads; i++) {
				SelectThread runnable = new SelectThread(this);
				runnable.setIterations(it);
				new Thread(runnable).start();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
