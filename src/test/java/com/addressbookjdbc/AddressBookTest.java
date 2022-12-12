package com.addressbookjdbc;
import static org.junit.Assert.*;
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
}
