package com.example.vasuchand.lbb_phonebook2;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.example.vasuchand.lbb_phonebook2.Adapters.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

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



        recyclerView.addOnItemTouchListener(
                new onclicklistener(context, recyclerView ,new onclicklistener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        data = content.get(position);
                        String id = data.getId();

                        Intent myIntent = new Intent(MainActivity.this, share_contact.class);
                        myIntent.putExtra("contactid", id); //Optional parameters

                        MainActivity.this.startActivity(myIntent);



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }
        else
        {
           contentResolver = getContentResolver();

            setupthings();
        }


    }

    public void fetch()
    {
        contentResolver = getContentResolver();

        setupthings();
    }

    public void setupthings()  {



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


        adapter = new adapter(MainActivity.this, content);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);
        // Retrieve the SearchView and plug it into SearchManager
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
                // **Here you can get the value "query" which is entered in the search box.**

                //Toast.makeText(getApplicationContext(),"searchvalue :"+query,Toast.LENGTH_LONG).show();

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

    //dialog permission box
    // currently made for one permission only
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //start audio recording or whatever you planned to do
                fetch();

            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Permission is to get your contacts")
                            .setTitle("LBB -Permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);

                        }
                    });
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
                }else{
                    //case when you already taken the permission
                    Log.d("MainActivity","No permission");

                    //Never ask again and handle your app without permission.
                }
            }
        }
    }

}
