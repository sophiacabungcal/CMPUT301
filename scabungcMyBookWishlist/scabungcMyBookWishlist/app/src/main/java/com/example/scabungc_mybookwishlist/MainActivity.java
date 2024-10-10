package com.example.scabungc_mybookwishlist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> bookList;
    private BookAdapter bookAdapter;
    private TextView totalBooks;
    private TextView totalReadBooks;
    private BookDialogHelper bookDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, this::showEditBookDialog);
        totalBooks = findViewById(R.id.totalBooks);
        totalReadBooks = findViewById(R.id.totalReadBooks);

        bookDialogHelper = new BookDialogHelper(this, bookList, bookAdapter, totalBooks, totalReadBooks);

        RecyclerView bookRecyclerView = findViewById(R.id.bookRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookRecyclerView.setAdapter(bookAdapter);
        bookRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Button addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(v -> bookDialogHelper.showAddBookDialog());
    }

    private void showEditBookDialog(int position) {
        bookDialogHelper.showEditBookDialog(position);
    }
}