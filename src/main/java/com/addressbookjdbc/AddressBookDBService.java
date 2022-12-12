package com.addressbookjdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
    private static AddressBookDBService addressBookDBService;

    private synchronized Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
        String userName = "root";
        String password = "Kruti77#";
        Connection connection;
        System.out.println("Connecting to database: " + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection successful!!" + connection);
        return connection;
    }

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    public List<Contacts> readData() {
        String query = "SELECT * FROM address_book;";
        List<Contacts> record = new ArrayList<Contacts>();
        try(Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                record.add(new Contacts(firstName, lastName, address,city, state, zip, phoneNumber, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }
}
