package com.addressbookjdbc;

public class AddressBookException extends Exception {

    public enum ExceptionType
    {UPDATE_FAIL,INSERTION_FAIL}

    public ExceptionType type;

    public AddressBookException(ExceptionType type,String message) {
        super(message);
        this.type = type;
    }
}
