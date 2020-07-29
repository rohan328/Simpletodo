package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE= 20;

    List<String> items;

    Button btnAdd;
    EditText edtItem;
    RecyclerView tdItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("To Do List");

        btnAdd = findViewById(R.id.btnAdd);
        edtItem = findViewById(R.id.edtItem);
        tdItems = findViewById(R.id.tdItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //delete item from model
                items.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item has been removed.", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //start edit activity
                Intent i = new Intent(MainActivity.this,editActivity.class);

                //pass the data to edit
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);

                //show new activity
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener,onClickListener);
        tdItems.setAdapter(itemsAdapter);
        tdItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = edtItem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                edtItem.setText("");
                Toast.makeText(getApplicationContext(), "Item has been added.", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    //handle the result of the add activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //Retrieve the updated data
            String item = data.getStringExtra(KEY_ITEM_TEXT);

            //extract the original position of edited item from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model with new item
            items.set(position, item);

            //notify the adapter
            itemsAdapter.notifyItemChanged(position);

            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "The item has been updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call the onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //this function will load items by reading the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    //this function will write items to the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity","error writing items",e);
        }
    }


}
