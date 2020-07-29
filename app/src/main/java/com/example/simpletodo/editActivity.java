package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class editActivity extends AppCompatActivity {

    EditText editText;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.editText);
        btnEdit = findViewById(R.id.btnEdit);

        getSupportActionBar().setTitle("Edit Item");

        editText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //the user clicks the save/edit button after editing the text
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create an intent which will contain the results
                Intent intent = new Intent();

                //pass the data
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editText.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //set results of intent
                setResult(RESULT_OK, intent);

                //finish activity and go back
                finish();

            }
        });
    }
}