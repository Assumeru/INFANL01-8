package nl.hro.infanl018.opdracht3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectThread extends ConnectionThread {
    private String voornaam;
    private String achternaam;

    public SelectThread(Main main) throws SQLException {
        super(main);
        //1
        getRandomStudent();

        //System.out.println(voornaam + achternaam);

        getConnection().commit();
    }

    @Override
     protected void doSomething() throws SQLException {
        //3 en 4
        getStudentModules();

        //5
        getConnection().commit();
    }

    private void getRandomStudent() throws SQLException{
        PreparedStatement p = getConnection().prepareStatement("SELECT id FROM personen WHERE voornaam = ? AND achternaam = ? LIMIT 1");
        p.setString(1, "Voornaam");
        p.setString(2, "Achternaam");

        ResultSet r = p.executeQuery();
        r.next();
        int studentId = r.getInt(1);

        r.close();

        p = getConnection().prepareStatement("UPDATE personen SET voornaam = ?, achternaam = ? WHERE id = ?");
        voornaam = "ABC" + Math.random();
        achternaam = "DEF" + Math.random();

        p.setString(1,  voornaam);
        p.setString(2, achternaam);
        p.setInt(3, studentId);
        p.execute();
    }

    private void getStudentModules() throws SQLException {
        PreparedStatement p = getConnection().prepareStatement("SELECT modules.*\n" +
                "FROM studenten\n" +
                "JOIN perioden ON (perioden.student = studenten.nummer)\n" +
                "JOIN planning ON (perioden.klas = planning.klas)\n" +
                "JOIN modules ON (modules.code = planning.module)\n" +
                "JOIN personen ON (personen.id = studenten.id)\n" +
                "WHERE personen.voornaam = ? AND personen.achternaam = ?");

        p.setString(1, voornaam);
        p.setString(2, achternaam);

        ResultSet r = p.executeQuery();
        StringBuilder stringBuilder = new StringBuilder();
        while(r.next()) {
            String moduleCode = r.getString(1);
            stringBuilder.append(moduleCode).append(" ");
        }
        r.close();

        //System.out.println("Student modules: " + stringBuilder.toString());
    }
}

