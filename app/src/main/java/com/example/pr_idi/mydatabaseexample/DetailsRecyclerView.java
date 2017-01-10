package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cmarchal on 1/10/17.
 */

public class DetailsRecyclerView extends RecyclerView {
    private List<Book> books;

    public DetailsRecyclerView(Context context, List<Book> books) {
        super(context);
        this.books = new ArrayList<>(books);
        Collections.sort(this.books, new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {
                return book1.getCategory().compareTo(book2.getCategory());
            }
        });
        this.setLayoutManager(new LinearLayoutManager(context));
        this.setAdapter(new DetailsAdapter());
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout fields;
        public ViewHolder(View itemView) {
            super(itemView);
            fields = (RelativeLayout) itemView;
        }
    }

    private class DetailsAdapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_view, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((TextView) holder.fields.findViewById(R.id.title_text_view)).setText(books.get(position).getTitle());
            ((TextView) holder.fields.findViewById(R.id.author_text_view)).setText(books.get(position).getAuthor());
            ((TextView) holder.fields.findViewById(R.id.cat_text_view)).setText(books.get(position).getCategory());
            String extra = books.get(position).getPublisher() + ", " + books.get(position).getYear();
            ((TextView) holder.fields.findViewById(R.id.year_text_view)).setText(extra);
        }

        @Override
        public int getItemCount() {
            return books.size();
        }
    }
}
