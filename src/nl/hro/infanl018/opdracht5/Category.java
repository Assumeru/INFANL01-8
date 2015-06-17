package nl.hro.infanl018.opdracht5;

public class Category {
	private Category parent;
	private String name;

	public Category(Category parent, String name) {
		this.parent = parent;
		this.name = name;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
