package nl.hro.infanl018.opdracht3;

import java.sql.SQLException;

public class InsertThread extends ConnectionThread {
	public InsertThread(String url, String username, String password) throws SQLException {
		super(url, username, password);
		setIterations(600);
	}

	@Override
	protected void doSomething() {
		//2
		student = addStudent();
		//3
		if(Math.random() < 0.033333 || !classExists()) {
			addClass();
		}
		//4
		addStudentToClass(student);
		//5
		if(Math.random() < 0.033333) {
			addModule();
		}
		//6
		getConnection().commit();
	}

	private void addStudent() {
		
	}

	private void addClass() {
		
	}

	private void addStudentToClass() {
		
	}

	private void addModule() {
		module = 
		for(classes) {
			if(Math.random() < 0.15) {
				addClassToModule(module, );
			}
		}
	}

	private void addClassToModule() {
		
	}

	private boolean classExists() {
		return false;
	}
}
