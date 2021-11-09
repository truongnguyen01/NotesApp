package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createnote extends AppCompatActivity {

    EditText mcreateTitleofNote, mcreateContentofNote;
    FloatingActionButton msaveNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressbarofcreatenote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);

        msaveNote = (FloatingActionButton) findViewById(R.id.savenote);
        mcreateTitleofNote = (EditText) findViewById(R.id.createtitleofnote);
        mcreateContentofNote = (EditText) findViewById(R.id.createcontentofnote);

        mprogressbarofcreatenote = (ProgressBar) findViewById(R.id.progressbarofcreatenote);

        Toolbar toolbar = findViewById(R.id.toolbarcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        msaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreateTitleofNote.getText().toString();
                String content = mcreateContentofNote.getText().toString();
                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Both field are require.", Toast.LENGTH_SHORT).show();
                }
                else{

                    mprogressbarofcreatenote.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note created successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createnote.this, notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create note.", Toast.LENGTH_SHORT).show();
                            mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                        }
                    });


                }
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}