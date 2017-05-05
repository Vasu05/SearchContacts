package com.example.vasuchand.lbb_phonebook2;

/**
 * Created by Vasu Chand on 5/4/2017.
 */

public class phonetype_data {
    private String phonetype,number;
    private int contacttype,view;

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getContacttype() {
        return contacttype;
    }

    public phonetype_data(int view,String phonetype, String number, int contacttype) {

        this.view = view;
        this.phonetype = phonetype;
        this.number = number;
        this.contacttype = contacttype;

    }

    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhonetype() {

        return phonetype;
    }

    public String getNumber() {
        return number;
    }
}
