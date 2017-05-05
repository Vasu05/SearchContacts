package com.example.vasuchand.lbb_phonebook2;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Import_contact extends AppCompatActivity {

    TextView importcontact,contactnumber,username;
    private int requestcode = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contact);
        importcontact = (TextView)findViewById(R.id.importcontacts);
        contactnumber = (TextView)findViewById(R.id.number);
        username =(TextView)findViewById(R.id.user_name);


        importcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Import_contact.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Import_contact.this, new String[]{Manifest.permission.READ_CONTACTS}, requestcode);
                }
                else {
                    importcontact();
                }
            }
        });

        Intent data = getIntent();
        String result=data.getStringExtra("contact");
        String result2=data.getStringExtra("username");
        contactnumber.setText(result);
        username.setText(result2);


    }

    public void importcontact()
    {

        Intent i = new Intent(Import_contact.this, MainActivity.class);
        startActivityForResult(i, requestcode);



    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == requestcode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                importcontact();

            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(Import_contact.this, Manifest.permission.READ_CONTACTS)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Permission is to get your contacts")
                            .setTitle("LBB -Permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(Import_contact.this, new String[]{Manifest.permission.READ_CONTACTS}, requestcode);

                        }
                    });
                    ActivityCompat.requestPermissions(Import_contact.this, new String[]{Manifest.permission.READ_CONTACTS}, requestcode);
                }else{
                    //case when you already taken the permission
                    Log.d("MainActivity","No permission");

                    //Never ask again and handle your app without permission.
                }
            }
        }
    }


   /*
    Because of more than one screen in history and need one so all other need to ignored when back click and exit the app.
    */

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
