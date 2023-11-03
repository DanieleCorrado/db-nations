package org.lesson.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main2 {

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        // Parametri di connessione

        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "root";

        Connection con = getConnection(url, user, password);

        System.out.print("Search country: ");
        String userSearch = "%" + scanner.nextLine() + "%";

        String query = "select c.country_id, c.name as country, " +
                "r.name as region_name, cs.name as continent_name " +
                "from countries c " +
                "join regions r on c.region_id = r.region_id " +
                "join continents cs on r.continent_id = cs.continent_id " +
                "where c.name like ? " +
                "order by c.name;";

        List<String> country = executeQuery(userSearch, con, query);

        for (int i = 0; i < country.size(); i++) {

            // Se l'elemento corrente è un numero, manda a capo
            if(i < country.size()-1) {
                if (isNumeric(country.get(i+1))) {
                    System.out.println();
                } else {
                    System.out.print(country.get(i) + " ");
                }
            } else {
                System.out.println();
            }
        }

        System.out.print("Enter the ID of the state you want more information about: ");
        String searchID = scanner.nextLine();

        String countryNameQuery = "select c.name " +
                "from countries c " +
                "where c.country_id = ?";

        List<String> countryName = executeQuery(searchID, con, countryNameQuery);

        System.out.println("Details for country: " + countryName.get(0));

        String languageQuery = "select l.language " +
                    "from country_languages cl " +
                    "join languages l on cl.language_id = l.language_id " +
                    "where cl.country_id = ?;";

        List<String> countryLanguages = executeQuery(searchID, con, languageQuery);

        System.out.print("Languages: ");

        String output = "";

        for (String element : countryLanguages) {

            // Aggiungi l'elemento corrente alla stringa di output
            output += element + ", ";
        }

        output = output.substring(0, output.length() - 2);

        System.out.println(output);

        String statQuery = "select cs.`year`, cs.population, cs.gdp " +
                    "from country_stats cs " +
                    "where cs.country_id = ? " +
                    "order by `year` desc " +
                    "limit 1;";

        List<String> countryStats = executeQuery(searchID, con, statQuery);

        System.out.println("Most recent stats");
        System.out.println("Year: " + countryStats.get(0));
        System.out.println("Population: " + countryStats.get(1));
        System.out.println("GDP: " + countryStats.get(2) + "$");

        closeConnection(con);

        // Provo a connettermi al DB

//        try(Connection con = DriverManager.getConnection(url, user, password)) {
//
//            System.out.println("what do you want to do?: ");
//            String userSearch = "%" + scanner.nextLine() + "%";
//
//            // Creo la query
//
//            String query = "select c.country_id, c.name as country, " +
//                    "r.name as region_name, cs.name as continent_name " +
//                    "from countries c " +
//                    "join regions r on c.region_id = r.region_id " +
//                    "join continents cs on r.continent_id = cs.continent_id " +
//                    "where c.name like ? " +
//                    "order by c.name;";
//
//
//            // Preparo la query
//            try(PreparedStatement ps = con.prepareStatement(query)) {
//
//                // Effettuo il binding dei parametri
//
//                ps.setString(1,userSearch);
//
//                // Eseguo il prepared statement
//
//                try(ResultSet rs = ps.executeQuery()) {
//
//                    while(rs.next()) {
//                        String countryName = rs.getString("country");
//                        int countryID = rs.getInt("country_id");
//                        String regionName = rs.getString("region_name");
//                        String continentName = rs.getString("continent_name");
//
//                        System.out.println(countryID + " " + countryName + " " + regionName + " " + continentName);
//                    }
//
//                } catch (SQLException e) {
//                    System.out.println("Unable to execute quey");
//                    e.printStackTrace();
//                }
//            } catch (SQLException e) {
//                System.out.println("Unable to prepare statement");
//                e.printStackTrace();
//            }
//
//            System.out.println("Enter the ID of the state you want more information about");
//            int searchID = Integer.parseInt(scanner.nextLine());
//
//
//            String countryNameQuery = "select c.name " +
//                    "from countries c " +
//                    "where c.country_id = ?";
//
//            try(PreparedStatement pslanguage = con.prepareStatement(countryNameQuery)) {
//
//                // Effettuo il binding dei parametri
//
//                pslanguage.setInt(1, searchID);
//
//                // Eseguo il prepared statement
//
//                try(ResultSet rsname = pslanguage.executeQuery()) {
//
//                    while(rsname.next()) {
//                        String language = rsname.getString("name");
//
//                        System.out.println("Details for country: " + language);
//                    }
//
//                } catch (SQLException e) {
//                    System.out.println("Unable to execute quey");
//                    e.printStackTrace();
//                }
//            } catch (SQLException e) {
//                System.out.println("Unable to prepare statement");
//                e.printStackTrace();
//            }
//
//            String languageQuery = "select l.language " +
//                    "from country_languages cl " +
//                    "join languages l on cl.language_id = l.language_id " +
//                    "where cl.country_id = ?;";
//
//            try(PreparedStatement pslanguage = con.prepareStatement(languageQuery)) {
//
//                // Effettuo il binding dei parametri
//
//                pslanguage.setInt(1, searchID);
//
//                // Eseguo il prepared statement
//
//                try(ResultSet rslanguage = pslanguage.executeQuery()) {
//
//                    List<String> languages = new ArrayList<>();
//
//                    System.out.print("Languages: ");
//                    while(rslanguage.next()) {
//                        languages.add(rslanguage.getString("language"));
//                    }
//
//                    for (int i = 0; i < languages.size(); i++) {
//                        System.out.print(languages.get(i));
//                        if (i < languages.size() -1) {
//                            System.out.print(", ");
//                        } else {
//                            System.out.println();
//                        }
//                    }
//
//                } catch (SQLException e) {
//                    System.out.println("Unable to execute quey");
//                    e.printStackTrace();
//                }
//            } catch (SQLException e) {
//                System.out.println("Unable to prepare statement");
//                e.printStackTrace();
//            }
//
//            String statQuery = "select cs.`year`, cs.population, cs.gdp " +
//                    "from country_stats cs " +
//                    "where cs.country_id = ? " +
//                    "order by `year` desc " +
//                    "limit 1;";
//
//            try(PreparedStatement psStat = con.prepareStatement(statQuery)) {
//
//                // Effettuo il binding dei parametri
//
//                psStat.setInt(1, searchID);
//
//                // Eseguo il prepared statement
//
//                try(ResultSet rsStat = psStat.executeQuery()) {
//
//                    System.out.println("Most recent stats");
//                    while(rsStat.next()) {
//                        int year = rsStat.getInt("year");
//                        int population = rsStat.getInt("population");
//                        long gdp = rsStat.getLong("gdp");
//
//                        System.out.println("Year: " + year);
//                        System.out.println("Population: " + population);
//                        System.out.println("GDP: " + gdp);
//
//                    }
//
//                } catch (SQLException e) {
//                    System.out.println("Unable to execute quey");
//                    e.printStackTrace();
//                }
//            } catch (SQLException e) {
//                System.out.println("Unable to prepare statement");
//                e.printStackTrace();
//            }
//
//
//        } catch (SQLException e) {
//            System.out.println("Unable to connect to database");
//            e.printStackTrace();
//        }
    
    }

    public static List<String> executeQuery(String userSearch, Connection con, String query) throws SQLException {

        List<String> answer = new ArrayList<>();
        // Preparo la query
        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1,userSearch);

            // Eseguo il prepared statement
            try (ResultSet rs = ps.executeQuery()) {

                int numColumns = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    // Stampa i valori delle colonne
                    for (int i = 1; i <= numColumns; i++) {
                        answer.add(rs.getString(i));
                    }
                }

            } catch (SQLException e) {
                System.out.println("Unable to execute query");
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("Unable to prepare statement");
            throw new SQLException();
        }
        return answer;
    }

    private static Connection getConnection(String url, String user, String password) throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static void closeConnection(Connection con) throws SQLException {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("[0-9]+");
    }

}
