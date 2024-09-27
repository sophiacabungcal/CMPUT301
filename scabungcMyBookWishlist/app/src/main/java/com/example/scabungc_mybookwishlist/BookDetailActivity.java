package com.example.scabungc_mybookwishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailActivity extends AppCompatActivity {
    private EditText etTitle, etAuthor, etGenre, etPublicationYear;
    private CheckBox cbRead;
    private Button btnSave, btnDelete;
    private Book book;
    private int bookIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etGenre = findViewById(R.id.etGenre);
        etPublicationYear = findViewById(R.id.etPublicationYear);
        cbRead = findViewById(R.id.cbRead);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        book = (Book) getIntent().getSerializableExtra("book");
        bookIndex = getIntent().getIntExtra("bookIndex", -1);

        if (book != null) {
            etTitle.setText(book.getTitle());
            etAuthor.setText(book.getAuthor());
            etGenre.setText(book.getGenre());
            etPublicationYear.setText(String.valueOf(book.getPublicationYear()));
            cbRead.setChecked(book.isRead());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTitle.getText().toString().isEmpty() || etAuthor.getText().toString().isEmpty() ||
                        etGenre.getText().toString().isEmpty() || etPublicationYear.getText().toString().isEmpty()) {
                    Toast.makeText(BookDetailActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                book.setTitle(etTitle.getText().toString());
                book.setAuthor(etAuthor.getText().toString());
                book.setGenre(etGenre.getText().toString());
                book.setPublicationYear(Integer.parseInt(etPublicationYear.getText().toString()));
                book.setRead(cbRead.isChecked());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedBook", book);
                resultIntent.putExtra("bookIndex", bookIndex);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deleteBookIndex", bookIndex);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
