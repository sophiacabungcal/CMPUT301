package com.example.scabungc_mybookwishlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private ArrayList<Book> bookList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public BookAdapter(ArrayList<Book> bookList, OnItemClickListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        if (position < 0 || position >= bookList.size()) {
            return;
        }
        Book book = bookList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(String.format(holder.itemView.getContext().getString(R.string.author_template), book.getAuthor()));
        holder.bookGenre.setText(String.format(holder.itemView.getContext().getString(R.string.genre_template), book.getGenre()));
        holder.bookReadStatus.setText(book.isBookRead() ? "Read" : "Un-read");
        int backgroundColor = book.isBookRead() ?
                holder.itemView.getContext().getColor(R.color.read_pink) :
                holder.itemView.getContext().getColor(R.color.unread_pink);
        holder.itemView.setBackgroundColor(backgroundColor);

        int textColor = book.isBookRead() ?
                holder.itemView.getContext().getColor(R.color.read_text) :
                holder.itemView.getContext().getColor(R.color.unread_text);
        holder.bookReadStatus.setTextColor(textColor);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitle;
        public TextView bookAuthor;
        public TextView bookGenre;
        public TextView bookReadStatus;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookGenre = itemView.findViewById(R.id.bookGenre);
            bookReadStatus = itemView.findViewById(R.id.bookReadStatus);
        }
    }
}