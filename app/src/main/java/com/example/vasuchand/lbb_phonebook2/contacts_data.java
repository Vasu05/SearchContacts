package com.example.vasuchand.lbb_phonebook2;



/**
 * Created by Vasu Chand on 5/3/2017.
 */
import android.net.Uri;

public class contacts_data
{
    private String number,name,id;
    Uri image;

    public void setImage(Uri image) {
        this.image = image;
    }

    public Uri getImage() {

        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public contacts_data(String name, Uri image, String id) {


        this.name = name;

        this.image = image;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {

        this.number = number;
    }

    public String getNumber() {

        return number;
    }

    public String getName() {
        return name;
    }


}
