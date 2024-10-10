package com.example.scabungc_mybookwishlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private boolean isBookRead;

    public Book(String title, String author, String genre, int publicationYear, boolean isBookRead) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isBookRead = isBookRead;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        genre = in.readString();
        publicationYear = in.readInt();
        isBookRead = in.readByte() != 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isBookRead() {
        return isBookRead;
    }

    public void setBookRead(boolean bookRead) {
        isBookRead = bookRead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(genre);
        parcel.writeInt(publicationYear);
        parcel.writeByte((byte) (isBookRead ? 1 : 0));
    }
}
