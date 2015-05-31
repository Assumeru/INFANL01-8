package nl.hro.infanl018.opdracht2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Graph extends JPanel {
	private List<Integer> points = new ArrayList<>();

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(points.isEmpty()) {
			return;
		}
		g.clearRect(0, 0, getWidth(), getHeight());
		int width = Math.max(1, getWidth() / points.size());
		int height = getHeight() / 2;
		int x = 0;
		int y = height;
		for(int i=0; i < points.size(); i++) {
			int nX = i * width;
			int nY = fitToY(height - points.get(i));
			g.drawLine(x, y, nX, nY);
			x = nX;
			y = nY;
		}
	}

	private int fitToY(int y) {
		if(y < 0) {
			return 0;
		} else if(y >= getHeight()) {
			return getHeight();
		}
		return y;
	}

	public void addPoint(int value) {
		if(points.size() == Integer.MAX_VALUE) {
			clearPoints();
		}
		points.add(value);
	}

	public void clearPoints() {
		points.clear();
	}
}
