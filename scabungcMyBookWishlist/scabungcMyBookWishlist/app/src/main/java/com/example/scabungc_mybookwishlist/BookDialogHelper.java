package com.example.scabungc_mybookwishlist;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class BookDialogHelper {

    private Context context;
    private ArrayList<Book> bookList;
    private BookAdapter bookAdapter;
    private TextView totalBooks;
    private TextView totalReadBooks;

    public BookDialogHelper(Context context, ArrayList<Book> bookList, BookAdapter bookAdapter, TextView totalBooks, TextView totalReadBooks) {
        this.context = context;
        this.bookList = bookList;
        this.bookAdapter = bookAdapter;
        this.totalBooks = totalBooks;
        this.totalReadBooks = totalReadBooks;
    }

    public void showAddBookDialog() {
        showBookDialog("Add Book to Wishlist", null, -1);
    }

    public void showEditBookDialog(int position) {
        showBookDialog("Edit Book", bookList.get(position), position);
    }

    private void showBookDialog(String title, Book book, int position) {
        // Method for how the dialog will be shown during add/edit/view mode

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        View view = View.inflate(context, R.layout.dialog_add_book, null);
        builder.setView(view);

        Spinner genreSpinner = view.findViewById(R.id.bookGenreSpinner);
        EditText genreOther = view.findViewById(R.id.bookGenreOther);
        setupGenreDropdown(genreSpinner, genreOther);

        if (book != null) {
            // Clicking on a book will automatically open in View mode where fields are non-editable

            populateDialogFields(view, book, genreSpinner, genreOther);
            setDialogFieldsEditable(view, false);
            builder.setTitle("Viewing Book Details");
            builder.setPositiveButton(R.string.edit_button, null);
        } else {
            // Adding a book opens the dialog directly in editable mode

            setDialogFieldsEditable(view, true);
            builder.setTitle("Adding New Book");
            builder.setPositiveButton(R.string.save_button, null);
        }

        builder.setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                if (validateInputs(view)) {
                    if (book != null) {
                        updateBook(view, dialog, book, position);
                    } else {
                        addBook(view, dialog);
                    }
                } else {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            });

            if (book != null) {
                // If the user clicks the edit btn in view mode, makes the fields editable and changes the title/btns

                Button editButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                editButton.setOnClickListener(v -> {
                    setDialogFieldsEditable(view, true);
                    dialog.setTitle("Editing Book Details");
                    editButton.setText(R.string.save_button);
                    editButton.setOnClickListener(saveView -> {
                        if (validateInputs(view)) {
                            updateBook(view, dialog, book, position);
                        } else {
                            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Button deleteButton = view.findViewById(R.id.deleteBookButton);
                    deleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setOnClickListener(deleteView -> confirmDeleteBook(position, dialog));
                });
            }
        });

        dialog.show();
    }

    private void setupGenreDropdown(Spinner genreSpinner, EditText genreOther) {
        // Configures the genre dropdown spinner and the "Other" genre field

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genreOther.setVisibility(position == parent.getCount() - 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genreOther.setVisibility(View.GONE);
            }
        });
    }

    private void setDialogFieldsEditable(View view, boolean editable) {
        // Configures the dialog fields to be editable or non-editable

        EditText bookTitle = view.findViewById(R.id.bookTitle);
        EditText bookAuthor = view.findViewById(R.id.bookAuthor);
        Spinner bookGenreSpinner = view.findViewById(R.id.bookGenreSpinner);
        EditText bookGenreOther = view.findViewById(R.id.bookGenreOther);
        EditText bookYear = view.findViewById(R.id.bookYear);
        CheckBox bookRead = view.findViewById(R.id.bookRead);

        bookTitle.setEnabled(editable);
        bookAuthor.setEnabled(editable);
        bookGenreSpinner.setEnabled(editable);
        bookGenreOther.setEnabled(editable);
        bookYear.setEnabled(editable);
        bookRead.setEnabled(editable);
    }

    private void populateDialogFields(View view, Book book, Spinner genreSpinner, EditText genreOther) {
        // Populates the dialog fields with the book's prev saved details

        ((EditText) view.findViewById(R.id.bookTitle)).setText(book.getTitle());
        ((EditText) view.findViewById(R.id.bookAuthor)).setText(book.getAuthor());
        ((EditText) view.findViewById(R.id.bookYear)).setText(String.valueOf(book.getPublicationYear()));
        ((CheckBox) view.findViewById(R.id.bookRead)).setChecked(book.isBookRead());

        int genrePos = Arrays.asList(context.getResources().getStringArray(R.array.genre_array)).indexOf(book.getGenre());

        if (genrePos == -1) {
            genreSpinner.setSelection(genreSpinner.getCount() - 1);
            genreOther.setText(book.getGenre());
            genreOther.setVisibility(View.VISIBLE);
        } else {
            genreSpinner.setSelection(genrePos);
        }
    }

    private void addBook(View view, AlertDialog dialog) {
        // Validates user input and adds a new book to the list

        if (validateInputs(view)) {
            Book newBook = createBookFromInputs(view);
            bookList.add(newBook);

            ((MainActivity) context).runOnUiThread(() -> {
                bookAdapter.notifyItemInserted(bookList.size() - 1);
                updateCounts();
            });
            dialog.dismiss();
        }
    }

    private void updateBook(View view, AlertDialog dialog, Book book, int position) {
        // Validates user input and updates the book in the list

        if (validateInputs(view)) {
            updateBookFromInputs(view, book);
            ((MainActivity) context).runOnUiThread(() -> {
                bookAdapter.notifyItemChanged(position);
                updateCounts();
            });
            dialog.dismiss();
        }
    }

    private void confirmDeleteBook(int position, AlertDialog dialog) {
        // Displays a confirmation dialog before deleting a book

        new AlertDialog.Builder(context)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this book?")
                .setPositiveButton(R.string.yes_button, (confirmDialog, which) -> {
                    bookList.remove(position);
                    bookAdapter.notifyItemRemoved(position);
                    bookAdapter.notifyItemRangeChanged(position, bookList.size());
                    updateCounts();
                    confirmDialog.dismiss();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel_button, (confirmDialog, which) -> confirmDialog.dismiss())
                .show();
    }

    private boolean validateInputs(View view) {
        // Validates the user input for the book details according to requierments
        // Returns true if all fields are valid, false otherwise

        String title = ((EditText) view.findViewById(R.id.bookTitle)).getText().toString();
        String author = ((EditText) view.findViewById(R.id.bookAuthor)).getText().toString();
        Spinner genreSpinner = view.findViewById(R.id.bookGenreSpinner);
        EditText genreOther = view.findViewById(R.id.bookGenreOther);
        String genre = genreSpinner.getSelectedItem().toString();

        String yearStr = ((EditText) view.findViewById(R.id.bookYear)).getText().toString();

        if (title.isEmpty() || title.length() > 50) {
            ((EditText) view.findViewById(R.id.bookTitle)).setError("Title must be 1-50 characters");
            return false;
        }
        if (author.isEmpty() || author.length() > 30) {
            ((EditText) view.findViewById(R.id.bookAuthor)).setError("Author must be 1-30 characters");
            return false;
        }
        if (genre.isEmpty() || (genre.equals("Other") && genreOther.getText().toString().length() > 20 && genreOther.getText().toString().isEmpty())) {
            if (genreSpinner.getSelectedItem().toString().equals("Other")) {
                genreOther.setError("Genre must be 1-20 characters");
            } else {
                ((TextView) genreSpinner.getSelectedView()).setError("Genre must be provided");
            }
            return false;
        }
        if (yearStr.length() != 4 || !yearStr.matches("\\d{4}")) {
            ((EditText) view.findViewById(R.id.bookYear)).setError("Year must be a 4-digit number");
            return false;
        }

        return true;
    }

    private Book createBookFromInputs(View view) {
        // Creates a new book object from the user input using the dialog fields

        String title = ((EditText) view.findViewById(R.id.bookTitle)).getText().toString();
        String author = ((EditText) view.findViewById(R.id.bookAuthor)).getText().toString();
        Spinner genreSpinner = view.findViewById(R.id.bookGenreSpinner);
        EditText genreOther = view.findViewById(R.id.bookGenreOther);
        String genre = genreSpinner.getSelectedItem().toString();
        if (genre.equals("Other")) {
            genre = genreOther.getText().toString();
        }
        String yearStr = ((EditText) view.findViewById(R.id.bookYear)).getText().toString();
        boolean isRead = ((CheckBox) view.findViewById(R.id.bookRead)).isChecked();
        int year = Integer.parseInt(yearStr);

        return new Book(title, author, genre, year, isRead);
    }

    private void updateBookFromInputs(View view, Book book) {
        // Updates the book object w/ the details from the dialog fields

        String title = ((EditText) view.findViewById(R.id.bookTitle)).getText().toString();
        String author = ((EditText) view.findViewById(R.id.bookAuthor)).getText().toString();
        Spinner genreSpinner = view.findViewById(R.id.bookGenreSpinner);
        EditText genreOther = view.findViewById(R.id.bookGenreOther);
        String genre = genreSpinner.getSelectedItem().toString();
        if (genre.equals("Other")) {
            genre = genreOther.getText().toString();
        }
        String yearStr = ((EditText) view.findViewById(R.id.bookYear)).getText().toString();
        boolean isRead = ((CheckBox) view.findViewById(R.id.bookRead)).isChecked();
        int year = Integer.parseInt(yearStr);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPublicationYear(year);
        book.setBookRead(isRead);
    }

    private void updateCounts() {
        // Updates the total book count and total read book count at the page header

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