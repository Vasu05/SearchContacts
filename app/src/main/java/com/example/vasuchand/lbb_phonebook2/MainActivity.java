package com.example.vasuchand.lbb_phonebook2;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.vasuchand.lbb_phonebook2.Adapters.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
    contacts_data data;
    public List<contacts_data> content= new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private  int REQUEST_CODE = 200;

    ContentProviderClient contentProviderClient;
    ContentResolver contentResolver;
    Cursor cursor;

    private static final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
    };

    Context context = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);


        contentResolver = getContentResolver();
        setupthings();
        adapter = new adapter(MainActivity.this, content);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }


    public void setupthings()
    {
        try {

            Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (cursor != null) {
                try {
                    final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    final int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

                    String name, number,id;
                        /*

                        hashset for getting unique mobile number for the user containing different numbers

                       */
                    HashSet<String> mobileNoSet = new HashSet<String>();
                    while (cursor.moveToNext()) {

                        name = cursor.getString(nameIndex);
                        number = cursor.getString(numberIndex);
                        number= number.replaceAll("[^0-9]","");

                        id = cursor.getString(contactIdIndex);

                        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                                .parseLong(id));

                        Uri contactphoto = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                        if (!mobileNoSet.contains(number))
                        {
                            data = new contacts_data(name, contactphoto,id);
                            content.add (data);
                            mobileNoSet.add(number);
                        }

                    }
                } finally {

                    cursor.close();
                }
            }


        } finally {
            // cursor.close();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);


        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search for Contacts");

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String input) {

                // Toast.makeText(getApplicationContext(),"textChanged :"+newText,Toast.LENGTH_LONG).show();
                input = input.toString().toLowerCase();
                List<contacts_data> temp = new ArrayList();

                for (contacts_data d : content) {

                     String str = d.getName().toLowerCase();

                    if (str.contains(input)) {
                        temp.add(d);

                    }
                }
                updateList(temp);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    /*
       filter recyclerview (contacts) list
     */



    /*
       Search Result
     */

    public void updateList(List<contacts_data> list){
        adapter = new adapter( MainActivity.this,list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onBackPressed() {
        // do something on back.

        Intent intent = new Intent(MainActivity.this , Import_contact.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }




}
