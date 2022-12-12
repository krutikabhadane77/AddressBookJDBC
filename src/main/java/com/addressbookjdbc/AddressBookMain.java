package com.addressbookjdbc;

import java.util.List;

public class AddressBookMain {
    public List<Contacts> record;
    private AddressBookDBService addressBookDBService;


    public AddressBookMain() {
        addressBookDBService=AddressBookDBService.getInstance();
    }


    public AddressBookMain(List<Contacts> record) {
        this();
        this.record = record;
    }

    public List<Contacts> readContactData() {
        record = addressBookDBService.readData();
        return record;
    }
}
