package com.example.scabungc_mybookwishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, this);
        recyclerView.setAdapter(bookAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Book newBook = (Book) data.getSerializableExtra("newBook");
            bookList.add(newBook);
            bookAdapter.notifyDataSetChanged();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Book updatedBook = (Book) data.getSerializableExtra("updatedBook");
            int bookIndex = data.getIntExtra("bookIndex", -1);
            if (bookIndex != -1) {
                bookList.set(bookIndex, updatedBook);
                bookAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            int deleteBookIndex = data.getIntExtra("deleteBookIndex", -1);
            if (deleteBookIndex != -1) {
                bookList.remove(deleteBookIndex);
                bookAdapter.notifyDataSetChanged();
            }
        }
    }
}