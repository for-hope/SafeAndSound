package com.forhope.sas;

/**
 * Created by lamine on 06/06/2017.
 */

public class Contacts {
private String contactName;
private String contactNumber;
private int id;

    public Contacts(){}

    public Contacts(String name, String number) {
        super();
        this.contactName = name;
        this.contactNumber = number;
    }


    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {

        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", id=" + id +
                '}';
    }
}
