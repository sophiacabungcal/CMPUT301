package com.example.scabungc_mybookwishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {
    private EditText etTitle, etAuthor, etGenre, etPublicationYear;
    private CheckBox cbRead;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etGenre = findViewById(R.id.etGenre);
        etPublicationYear = findViewById(R.id.etPublicationYear);
        cbRead = findViewById(R.id.cbRead);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTitle.getText().toString().isEmpty() || etAuthor.getText().toString().isEmpty() ||
                        etGenre.getText().toString().isEmpty() || etPublicationYear.getText().toString().isEmpty()) {
                    Toast.makeText(AddBookActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String genre = etGenre.getText().toString();
                int publicationYear = Integer.parseInt(etPublicationYear.getText().toString());
                boolean isRead = cbRead.isChecked();

                Book newBook = new Book(title, author, genre, publicationYear, isRead);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newBook", newBook);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
