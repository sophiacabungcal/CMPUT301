package com.example.scabungc_mybookwishlist;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private TextView totalBooks;
    private TextView totalReadBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList, this::showEditBookDialog);

        RecyclerView bookRecyclerView = findViewById(R.id.bookRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookRecyclerView.setAdapter(adapter);
        bookRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        totalBooks = findViewById(R.id.totalBooks);
        totalReadBooks = findViewById(R.id.totalReadBooks);

        Button addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(v -> showAddBookDialog());
    }

    private void showAddBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Book to Wishlist");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_book, null);
        builder.setView(view);

        Spinner genreSpinner = view.findViewById(R.id.bookGenreSpinner);
        EditText genreOther = view.findViewById(R.id.bookGenreOther);

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount() - 1) { // "Other" is the last item
                    genreOther.setVisibility(View.VISIBLE);
                } else {
                    genreOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genreOther.setVisibility(View.GONE);
            }
        });

        builder.setPositiveButton("Add", null);

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            // Add logic; validate inputs, add new book to the list + updates counts
            addButton.setOnClickListener(v -> {

                String title = ((EditText) view.findViewById(R.id.bookTitle)).getText().toString();
                String author = ((EditText) view.findViewById(R.id.bookAuthor)).getText().toString();
                String genre = genreSpinner.getSelectedItem().toString();
                if (genre.equals("Other")) {
                    genre = genreOther.getText().toString();
                }
                String yearStr = ((EditText) view.findViewById(R.id.bookYear)).getText().toString();
                boolean isRead = ((CheckBox) view.findViewById(R.id.bookRead)).isChecked();

                // Input validation
                if (title.isEmpty() || title.length() > 50) {
                    ((EditText) view.findViewById(R.id.bookTitle)).setError("Title must be 1-50 characters");
                    return;
                }
                if (author.isEmpty() || author.length() > 30) {
                    ((EditText) view.findViewById(R.id.bookAuthor)).setError("Author must be 1-30 characters");
                    return;
                }
                if (genre.isEmpty()) {
                    if (genreSpinner.getSelectedItem().toString().equals("Other")) {
                        genreOther.setError("Genre must be provided");
                    } else {
                        ((TextView) genreSpinner.getSelectedView()).setError("Genre must be provided");
                    }
                    return;
                }
                if (yearStr.length() != 4 || !yearStr.matches("\\d{4}")) {
                    ((EditText) view.findViewById(R.id.bookYear)).setError("Year must be a 4-digit number");
                    return;
                }
                int year = Integer.parseInt(yearStr);

                Book newBook = new Book(title, author, genre, year, isRead);
                bookList.add(newBook);
                runOnUiThread(() -> {
                    adapter.notifyItemInserted(bookList.size() - 1);
                    updateCounts();
                });
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showEditBookDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Book");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_book, null);
        builder.setView(view);

        Book book = bookList.get(position);
        ((EditText) view.findViewById(R.id.bookTitle)).setText(book.getTitle());
        ((EditText) view.findViewById(R.id.bookAuthor)).setText(book.getAuthor());
        Spinner genreSpinner = view.findViewById(R.id.bookGenreSpinner);
        EditText genreOther = view.findViewById(R.id.bookGenreOther);
        int genrePosition = Arrays.asList(getResources().getStringArray(R.array.genre_array)).indexOf(book.getGenre());
        if (genrePosition == -1) {
            genreSpinner.setSelection(genreSpinner.getCount() - 1);
            genreOther.setText(book.getGenre());
            genreOther.setVisibility(View.VISIBLE);
        } else {
            genreSpinner.setSelection(genrePosition);
        }
        ((EditText) view.findViewById(R.id.bookYear)).setText(String.valueOf(book.getPublicationYear()));
        ((CheckBox) view.findViewById(R.id.bookRead)).setChecked(book.isBookRead());

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount() - 1) {
                    genreOther.setVisibility(View.VISIBLE);
                } else {
                    genreOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genreOther.setVisibility(View.GONE);
            }
        });

        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveButton.setOnClickListener(v -> {
                String title = ((EditText) view.findViewById(R.id.bookTitle)).getText().toString();
                String author = ((EditText) view.findViewById(R.id.bookAuthor)).getText().toString();
                String genre = genreSpinner.getSelectedItem().toString();
                if (genre.equals("Other")) {
                    genre = genreOther.getText().toString();
                }
                String yearStr = ((EditText) view.findViewById(R.id.bookYear)).getText().toString();
                boolean isRead = ((CheckBox) view.findViewById(R.id.bookRead)).isChecked();

                if (title.isEmpty() || title.length() > 50) {
                    ((EditText) view.findViewById(R.id.bookTitle)).setError("Title must be 1-50 char");
                    return;
                }
                if (author.isEmpty() || author.length() > 30) {
                    ((EditText) view.findViewById(R.id.bookAuthor)).setError("Author must be 1-30 char");
                    return;
                }
                if (genre.isEmpty()) {
                    if (genreSpinner.getSelectedItem().toString().equals("Other")) {
                        genreOther.setError("Genre must be provided");
                    } else {
                        ((TextView) genreSpinner.getSelectedView()).setError("Genre must be provided");
                    }
                    return;
                }
                if (yearStr.length() != 4 || !yearStr.matches("\\d{4}")) {
                    ((EditText) view.findViewById(R.id.bookYear)).setError("Year must be a 4-digit number");
                    return;
                }
                int year = Integer.parseInt(yearStr);

                book.setTitle(title);
                book.setAuthor(author);
                book.setGenre(genre);
                book.setPublicationYear(year);
                book.setBookRead(isRead);

                runOnUiThread(() -> {
                    adapter.notifyItemChanged(position);
                    updateCounts();
                });
                dialog.dismiss();
            });
        });

        Button deleteButton = view.findViewById(R.id.deleteBookButton);
        deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this book?")
                    .setPositiveButton("Yes", (confirmDialog, which) -> {
                        bookList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, bookList.size());
                        updateCounts();
                        confirmDialog.dismiss();
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (confirmDialog, which) -> confirmDialog.dismiss())
                    .show();
        });

        dialog.show();
    }

    private void updateCounts() {
        totalBooks.setText(String.format("Total # of Books: %d", bookList.size()));
        int readBooksCount = 0;
        for (Book book : bookList) {
            if (book.isBookRead()) {
                readBooksCount++;
            }
        }
        totalReadBooks.setText(String.format("Total # of Read Books: %d", readBooksCount));
    }
}