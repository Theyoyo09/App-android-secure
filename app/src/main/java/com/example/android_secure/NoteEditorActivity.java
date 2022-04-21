package com.example.android_secure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);

        // Fetch data that is passed from MainActivity
        Intent intent = getIntent();

        // Accessing the data using key and value
        noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            editText.setText(MainActivity.notes.get(noteId));
        } else {

            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();

        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // add your code here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                // Creating Object of SharedPreferences to store data in the phone
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                //HashSet<String> set = new HashSet(MainActivity.notes);
                String result = MainActivity.encrypt(MainActivity.notes.toString());
             //   System.out.print(result);
                content = MainActivity.notes.toString();
                System.out.print(content);
                sharedPreferences.edit().putString("notes", result).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // add your code here
            }
        });
    }

    public void Retour(View view) {
        finish();
    }

    public void Exportation(View view) throws Exception{

        try {
            String rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/Download/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            File f = new File(rootPath + "Exportage_note.txt");
            if (f.exists()) {
                f.delete();
            }


            f.createNewFile();


            try (FileOutputStream fos = new FileOutputStream(f);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {


                // write object to file
                oos.writeObject(content);

        } catch (Exception e) {
            e.printStackTrace();
        }

    } catch (IOException e) {
            e.printStackTrace();
        }
    }}
