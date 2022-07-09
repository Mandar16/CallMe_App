package com.example.contacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import Data.MyDbHandler;
import Model.Contact;
import adaptor.RecyclerViewAdapter;


public class MainActivity extends AppCompatActivity {
   private RecyclerView recyclerView;
    private  RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Contact> contactArrayList;
    private ArrayAdapter<String> arrayAdapter;
    Toolbar mTopToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopToolbar = findViewById(R.id.toolbar);
       setSupportActionBar(mTopToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        //Recycler View initialization


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        MyDbHandler db = new MyDbHandler(MainActivity.this);






        List<Contact> allContacts = new ArrayList<>();
        allContacts = db.getAllContacts();
        //Log.d("harry",""+ allContacts.get(0).getName());

        contactArrayList = new ArrayList<>();

        for (Contact contact : allContacts) {
            // Log.d("harry","is this running" );
            // Log.d("harry", "\nId: " + contact.getId() + "\n" +
            // "Name: " + contact.getName() + "\n"+
            //"Phone Number: " + contact.getPhoneNumber() + "\n" );
            contactArrayList.add(contact);

        }
        //Using recycler view
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, contactArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar,menu);
        inflater.inflate(R.menu.save_icon, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView)searchViewItem.getActionView();
        final SearchView searchView1 = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchView1.setBackgroundColor(Color.TRANSPARENT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView1.setBackgroundColor(Color.TRANSPARENT);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    searchView1.setBackgroundColor(Color.TRANSPARENT);
                }
                else{
                    searchView1.setBackgroundColor(Color.rgb(31,31,31));
                }
                recyclerViewAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        MyDbHandler db = new MyDbHandler(MainActivity.this);
        if(item.getItemId() == R.id.action_edit){
            Intent intent = new Intent(this, Enter_Data.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}