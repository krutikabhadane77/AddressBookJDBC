package com.addressbookjdbc;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
public class AddressBookTest {
    AddressBookMain addressBookFunction;
    List<Contacts> record;

    @Before
    public void init() {
        addressBookFunction=new AddressBookMain();
        record=addressBookFunction.readContactData();
    }

    @Test
    public void givenAddressBook_WhenRetrieved_ShouldMatchCount() {
        assertEquals(2, record.size());
    }

    @Test
    public void givenNewAddressForRecord_WhenUpdated_ShouldSyncWithDatabase() throws AddressBookException {
        List<Contacts>contactData=addressBookFunction.readContactData();
        addressBookFunction.updateRecord("Krutika","Jalgaon");
        boolean result=addressBookFunction.checkAddressBookInSyncWithDB("Krutika");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRangeForRecord_WhenRetrieved_ShouldReturnProperData() throws AddressBookException {
        List<Contacts> recordDataInGivenDateRange = addressBookFunction.getRecordAddedInDateRange("2018-01-01","2019-11-30");
        assertEquals(0, recordDataInGivenDateRange.size());
    }

    @Test
    public void givenCityOrState_WhenRetrieved_ShouldReturnProperData() throws AddressBookException {
        List<Contacts> recordDataByCityState = addressBookFunction.getRecordsByCityOrState("Jalgaon", "Maharashtra");
        assertEquals(1, recordDataByCityState.size());
    }

    @Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDB() throws AddressBookException {
        addressBookFunction.addContactToRecord("Ved", "Patil", "Mall Road",
                "Surat", "Gujrat", "245694", "8456215596", "vp@gmail.com");
        assertTrue(addressBookFunction.checkAddressBookInSyncWithDB("Ved"));
    }
}
