package nl.hro.infanl018.opdracht3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertThread extends ConnectionThread {
	private static final String[] geslachten = new String[] {
		"man", "vrouw", "onbepaald", "onbekend"
	};
	private int lastClass;
	private String lastTeacher;

	public InsertThread(Main main) throws SQLException {
		super(main);
	}

	@Override
	protected void doSomething() throws SQLException {
		//2
		String student = addStudent();
		//3
		if(Math.random() < 0.033333 || lastClass == 0) {
			lastClass = addClass();
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

	private int addPerson() throws SQLException {
		PreparedStatement p = getConnection().prepareStatement("INSERT INTO personen (voornaam, achternaam, geboortedatum, geslacht, postcode, telefoonnummer) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		p.setString(1, "Voornaam");
		p.setString(2, "Achternaam");
		p.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		p.setString(4, geslachten[(int)(Math.random() * geslachten.length)]);
		p.setString(5, (int)(1000+Math.random()*8999)+"XX");
		p.setString(6, "---");
		p.execute();
		ResultSet r = p.getGeneratedKeys();
		r.next();
		int id = r.getInt(r.findColumn("id"));
		r.close();
		return id;
	}

	private String addStudent() throws SQLException {
		int id = addPerson();
		PreparedStatement p = getConnection().prepareStatement("INSERT INTO studenten VALUES(?, ?)");
		p.setInt(1, id);
		String nummer = ""+id;
		while(nummer.length() < 7) {
			nummer = '-'+nummer;
		}
		p.setString(2, nummer);
		p.execute();
		return nummer;
	}

	private int addClass() throws SQLException {
		PreparedStatement p = getConnection().prepareStatement("INSERT INTO klassen (naam, startdatum, einddatum) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		p.setString(1, "klas");
		p.setDate(2, new java.sql.Date(1234567));
		p.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		p.execute();
		ResultSet r = p.getGeneratedKeys();
		r.next();
		int id = r.getInt(r.findColumn("id"));
		r.close();
		return id;
	}

	private void addStudentToClass(String student) throws SQLException {
		PreparedStatement p = getConnection().prepareStatement("INSERT INTO perioden VALUES(?, ?)");
		p.setString(1, student);
		p.setInt(2, lastClass);
		p.execute();
	}

	private String addTeacher() throws SQLException {
		int id = addPerson();
		PreparedStatement p = getConnection().prepareStatement("INSERT INTO docenten VALUES(?, ?)");
		p.setInt(1, id);
		String nummer = ""+id;
		while(nummer.length() < 7) {
			nummer = '-'+nummer;
		}
		p.setString(2, nummer);
		p.execute();
		return nummer;
	}

	private void addModule() throws SQLException {
		if(lastTeacher == null) {
			lastTeacher = addTeacher();
		}
		String module = getCode(System.currentTimeMillis());
		PreparedStatement p = getConnection().prepareStatement("INSERT INTO modules VALUES(?, ?, ?, ?, ?)");
		p.setString(1, module);
		p.setString(2, "module");
		p.setString(3, lastTeacher);
		p.setDate(4, new java.sql.Date(1234567));
		p.setDate(5, new java.sql.Date(System.currentTimeMillis()));
		p.execute();

		p = getConnection().prepareStatement("SELECT id FROM klassen");
		ResultSet r = p.executeQuery();
		while(r.next()) {
			int id = r.getInt(1);
			if(Math.random() < 0.15) {
				addClassToModule(module, id);
			}
		}
		r.close();
	}

	private String getCode(long num) {
		StringBuilder sb = new StringBuilder();
		char[] nums = String.valueOf(num).toCharArray();
		for(char c : nums) {
			sb.appendCodePoint(c+17);
		}
		return sb.toString();
	}

	private void addClassToModule(String module, int classId) throws SQLException {
		PreparedStatement p = getConnection().prepareStatement("INSERT INTO planning VALUES(?, ?)");
		p.setInt(1, classId);
		p.setString(2, module);
		p.execute();
	}
}
