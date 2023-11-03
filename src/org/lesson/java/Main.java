package org.lesson.java;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Parametri di connessione

        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "root";

        // Provo a connettermi al DB

        try(Connection con = DriverManager.getConnection(url, user, password)) {

            System.out.print("Enter string: ");
            String userSearch = "%" + scanner.nextLine() + "%";

            // Creo la query

            String query = "select c.country_id, c.name as country, " +
                    "r.name as region_name, cs.name as continent_name " +
                    "from countries c " +
                    "join regions r on c.region_id = r.region_id " +
                    "join continents cs on r.continent_id = cs.continent_id " +
                    "where c.name like ? " +
                    "order by c.name;";

            // Preparo la query
            try(PreparedStatement ps = con.prepareStatement(query)) {

                // Effettuo il binding dei parametri

                ps.setString(1,userSearch);

                // Eseguo il prepared statement

                try(ResultSet rs = ps.executeQuery()) {

                    while(rs.next()) {
                        String countryName = rs.getString("country");
                        int countryID = rs.getInt("country_id");
                        String regionName = rs.getString("region_name");
                        String continentName = rs.getString("continent_name");

                        System.out.println(countryID + " " + countryName + " " + regionName + " " + continentName);
                    }
                } catch (SQLException e) {
                    System.out.println("Unable to execute quey");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Unable to prepare statement");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Unable to connect to database");
            e.printStackTrace();
        }
    }
}
