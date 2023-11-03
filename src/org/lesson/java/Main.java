package org.lesson.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

            System.out.println("Enter the ID of the state you want more information about");
            int searchID = Integer.parseInt(scanner.nextLine());


            String countryNameQuery = "select c.name " +
                    "from countries c " +
                    "where c.country_id = ?";

            try(PreparedStatement pslanguage = con.prepareStatement(countryNameQuery)) {

                // Effettuo il binding dei parametri

                pslanguage.setInt(1, searchID);

                // Eseguo il prepared statement

                try(ResultSet rsname = pslanguage.executeQuery()) {

                    while(rsname.next()) {
                        String language = rsname.getString("name");

                        System.out.println("Details for country: " + language);
                    }

                } catch (SQLException e) {
                    System.out.println("Unable to execute quey");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Unable to prepare statement");
                e.printStackTrace();
            }

            String languageQuery = "select l.language " +
                    "from country_languages cl " +
                    "join languages l on cl.language_id = l.language_id " +
                    "where cl.country_id = ?;";

            try(PreparedStatement pslanguage = con.prepareStatement(languageQuery)) {

                // Effettuo il binding dei parametri

               pslanguage.setInt(1, searchID);

                // Eseguo il prepared statement

                try(ResultSet rslanguage = pslanguage.executeQuery()) {

                    List<String> languages = new ArrayList<>();

                    System.out.print("Languages: ");
                    while(rslanguage.next()) {
                        languages.add(rslanguage.getString("language"));
                    }

                    for (int i = 0; i < languages.size(); i++) {
                        System.out.print(languages.get(i));
                        if (i < languages.size() -1) {
                            System.out.print(", ");
                        } else {
                            System.out.println();
                        }
                    }

                } catch (SQLException e) {
                    System.out.println("Unable to execute quey");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Unable to prepare statement");
                e.printStackTrace();
            }

            String statQuery = "select cs.`year`, cs.population, cs.gdp " +
                    "from country_stats cs " +
                    "where cs.country_id = ? " +
                    "order by `year` desc " +
                    "limit 1;";

            try(PreparedStatement psStat = con.prepareStatement(statQuery)) {

                // Effettuo il binding dei parametri

                psStat.setInt(1, searchID);

                // Eseguo il prepared statement

                try(ResultSet rsStat = psStat.executeQuery()) {

                    System.out.println("Most recent stats");
                    while(rsStat.next()) {
                        int year = rsStat.getInt("year");
                        int population = rsStat.getInt("population");
                        long gdp = rsStat.getLong("gdp");

                        System.out.println("Year: " + year);
                        System.out.println("Population: " + population);
                        System.out.println("GDP: " + gdp);

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
