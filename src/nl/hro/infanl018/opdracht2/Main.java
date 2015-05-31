package nl.hro.infanl018.opdracht2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Main {
	private JFrame window;
	private JSpinner numThreads;
	private boolean running = false;
	private ThreadManager manager;
	private Connection conn;
	private Graph graph;

	public static void main(String[] args) throws SQLException {
		if(args.length != 3) {
			System.out.println("Usage: URL USERNAME PASSWORD");
			System.exit(1);
		}
		new Main(args[0], args[1], args[2]);
	}

	public Main(String url, String username, String password) throws SQLException {
		initWindow();
		initInterface();
		conn = DriverManager.getConnection(url, username, password);
		manager = new ThreadManager(this, url, username, password);
		window.setVisible(true);
	}

	private void initWindow() {
		window = new JFrame();
		window.setTitle("Opdracht 2 - INFANL01-8 - Advanced Databases");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(600, 400);
		window.setLocationRelativeTo(null);
	}

	private void initInterface() {
		JPanel body = new JPanel();
		JPanel panel = new JPanel();
		body.setLayout(new BorderLayout());
		panel.setLayout(new GridLayout(0, 4));

		SpinnerNumberModel numThreadsModel = new SpinnerNumberModel(2, 1, null, 1);
		numThreads = new JSpinner(numThreadsModel);

		panel.add(new JLabel("Hoeveelheid threads: "));
		panel.add(numThreads);
		final JButton run = new JButton("Start");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(running) {
					run.setText("Start");
					manager.setNumThreads(0);
					graph.clearPoints();
				} else {
					run.setText("Stop");
					manager.setNumThreads((Integer)numThreads.getValue());
				}
				running = !running;
			}
		});

		final JButton clear = new JButton("Leeg veranderingen");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearDatabase();
				System.out.println("De veranderingentabel is geleegd.");
			}
		});

		graph = new Graph();
		graph.setSize(body.getSize());

		panel.add(run);
		panel.add(clear);
		body.add(panel, BorderLayout.NORTH);
		body.add(graph, BorderLayout.CENTER);
		window.add(body);
	}

	private void clearDatabase() {
		try {
			conn.prepareCall("DELETE FROM veranderingen").execute();
		} catch (SQLException e) {
			System.err.println("Kon niet legen, sorry.");
		}
	}

	public void addPointToGraph(int delta) {
		graph.addPoint(delta);
		graph.repaint();
	}
}
