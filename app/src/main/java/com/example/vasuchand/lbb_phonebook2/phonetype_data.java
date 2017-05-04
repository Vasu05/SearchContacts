package com.example.vasuchand.lbb_phonebook2;

/**
 * Created by Vasu Chand on 5/4/2017.
 */

public class phonetype_data {
    private String phonetype,number;



    public phonetype_data(String phonetype, String number) {

        this.phonetype = phonetype;
        this.number = number;
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
