package nl.hro.infanl018.opdracht2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public class Graph extends JPanel {
	private Map<Integer, List<Integer>> lines = new HashMap<>();

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(lines.isEmpty()) {
			return;
		}
		g.clearRect(0, 0, getWidth(), getHeight());
		int height = getHeight() / 2;
		for(Map.Entry<Integer, List<Integer>> pair : lines.entrySet()) {
			Integer[] points = pair.getValue().toArray(new Integer[0]);
			int type = pair.getKey();
			if(points.length != 0) {
				int width = Math.max(1, getWidth() / points.length);
				g.setColor(getColor(type));
				int x = 0;
				int y = height;
				for(int i=0; i < points.length; i++) {
					Integer point = points[i];
					if(point != null) {
						int nX = i * width;
						int nY = fitToY(height - point + type);
						g.drawLine(x, y, nX, nY);
						x = nX;
						y = nY;
					}
				}
			}
		}
	}

	public Color getColor(int type) {
		switch(type) {
			case 0:
				return Color.RED;
			case 1:
				return Color.GREEN;
			case 2:
				return Color.BLUE;
			case 3:
				return Color.YELLOW;
			default:
				return Color.BLACK;
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

	public void addPoint(int type, int value) {
		if(!lines.containsKey(type)) {
			lines.put(type, Collections.synchronizedList(new ArrayList<Integer>()));
		}
		List<Integer> points = lines.get(type);
		try {
			points.add(value);
			if(getWidth() / points.size() <= 2) {
				List<Integer> newPoints = points.subList(10, points.size());
				points.clear();
				points.addAll(newPoints);
			}
		} catch(ConcurrentModificationException e) {}
	}

	public void clearPoints() {
		lines.clear();
	}
}
