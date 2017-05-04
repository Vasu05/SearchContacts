package com.example.vasuchand.lbb_phonebook2;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vasuchand.lbb_phonebook2.Adapters.adapter;
import com.example.vasuchand.lbb_phonebook2.Adapters.phonetype_adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Vasu Chand on 5/3/2017.
 */

public class share_contact extends AppCompatActivity {

    ContentProviderClient contentProviderClient;
    ContentResolver contentResolver;
    Cursor cursor;

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    phonetype_data data;
    public List<phonetype_data> content= new ArrayList<>();

    ImageView image ;
    Context context = share_contact.this;
    TextView user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendcontact);

       /*
          Recylerview
          for the different types of mobile number

         */

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new phonetype_adapter(share_contact.this, content);


        image = (ImageView)findViewById(R.id.image);
        user_name = (TextView)findViewById(R.id.user_name);

        Intent intent = getIntent();
        String contactid = intent.getStringExtra("contactid");


        if(contactid!=null) {
            contentResolver = getContentResolver();

            contentProviderClient = contentResolver.acquireContentProviderClient(ContactsContract.Contacts.CONTENT_URI);

            try {
                cursor = contentProviderClient.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                        new String[]{contactid}, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

                HashSet<String> mobileNoSet = new HashSet<String>();
                while (cursor.moveToNext())
                {

                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    number= number.replaceAll("[^0-9]","");

                    /*
                       username
                     */
                    user_name.setText(name);

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                            .parseLong(contactid));

                    Uri contactphoto = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    /*
                    if user has image saved in contact
                    */
                    setImage(contactphoto);

                    int type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    String types = "";

                    switch (type) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            types = "Home";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            types = "Mobile";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                            types = "Work";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                            types = "Home Fax";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                            types = "Work Fax";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                            types = "Main";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                            types = "Other";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                            types = "Custom";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                            types = "Pager";
                            break;
                    }
                    if (!mobileNoSet.contains(number)) {
                        data = new phonetype_data(types,number);
                        content.add(data);
                        mobileNoSet.add(number);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    public void setImage(Uri uri)
    {
        Bitmap bitmap=null;
        try {
            if(uri!=null) {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap!=null) {
            image.setImageBitmap(bitmap);
        }
    }
}
