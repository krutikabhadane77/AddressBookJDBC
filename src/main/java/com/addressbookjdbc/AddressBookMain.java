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

    private Contacts getRecordDataByName(String firstName) {
        Contacts contactData = this.record.stream()
                .filter(contact->contact.firstName.equals(firstName))
                .findFirst()
                .orElse(null);
        return contactData;
    }

    public void updateRecord(String firstName, String city) throws AddressBookException {
        int result = addressBookDBService.updateData(firstName,city);
        if(result == 0)
            return;
        Contacts contactData=this.getRecordDataByName(firstName);
        if (contactData!=null){
            contactData.city=city;
        }
    }

    public boolean checkAddressBookInSyncWithDB(String firstName) {
        List<Contacts> checkList = addressBookDBService.getRecordDataByName(firstName);
        return checkList.get(0).equals(getRecordDataByName(firstName));
    }
}
