package com.addressbookjdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
    private static AddressBookDBService addressBookDBService;
    private PreparedStatement recordStatement;

    public enum StatementType
    {PREPARED_STATEMENT, STATEMENT}

    private void preparedStatementForRecord() {
        try {
            Connection connection = this.getConnection();
            String query = "SELECT * FROM address_book WHERE firstName = ?";
            recordStatement = connection.prepareStatement(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
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
        return getAddressBookData(query);
    }
    public List<Contacts> getAddressBookData(String query) {
        List<Contacts> record = new ArrayList<Contacts>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            record = this.getAddressBookData(resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return record;
    }
    private List<Contacts>getAddressBookData(ResultSet resultSet){
        List<Contacts>contactsList=new ArrayList<>();
        try {
            while(resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                contactsList.add(new Contacts(firstName, lastName, address,city, state, zip, phoneNumber, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    public List<Contacts> getRecordDataByName(String firstName) {
        List<Contacts> record = null;
        if (this.recordStatement == null) this.preparedStatementForRecord();
        try {
            recordStatement.setString(1, firstName);
            ResultSet resultSet = recordStatement.executeQuery();
            record = this.getAddressBookData(resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return record;
    }

    public int updateData(String firstName, String city)throws AddressBookException{
        return this.updateDataUsingPreparedStatement(firstName,city);
    }

    public int updateDataUsingPreparedStatement(String firstName, String city) {
        try (Connection connection = this.getConnection();) {
            String query = "UPDATE address_book SET city= ? WHERE firstName= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, city);
            preparedStatement.setString(2, firstName);
            int status= preparedStatement.executeUpdate();
            return status;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public List<Contacts> getRecordsAddedInGivenDateRange(String date1, String date2) {
        String query = String.format("SELECT * FROM address_book WHERE dateAdded BETWEEN '%s' AND '%s';", date1, date2);
        return this.getAddressBookData(query);
    }

    public List<Contacts> getRecordsByCityOrState(String city, String state) {
        String query = String.format("SELECT * FROM address_book WHERE city='%s' OR state='%s';", city, state);
        return this.getAddressBookData(query);
    }

    public Contacts addContactToRecord(String firstName, String lastName, String address, String city, String state, String zip,
                                       String phoneNumber, String email) throws AddressBookException {
        int contactId = -1;
        Connection connection = null;
        Contacts addressBookData = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        }catch(SQLException exception) {
            exception.printStackTrace();
        }
        try(Statement statement = connection.createStatement()){
            String query = String.format("INSERT INTO address_book VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')",
                    firstName, lastName, address, city, state, zip,
                    phoneNumber, email);
            int rowAffected = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            if(rowAffected==1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) contactId =  resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            try {
                connection.rollback();
                return addressBookData;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new AddressBookException(AddressBookException.ExceptionType.INSERTION_FAIL, "Insertion to DB failed");
        }
        return addressBookData;
    }
}
