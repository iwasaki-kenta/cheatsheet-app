package com.dranithix.cheatsheet.ui;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dranithix.cheatsheet.R;
import com.dranithix.cheatsheet.entities.Category;
import com.dranithix.cheatsheet.entities.Note;
import com.dranithix.cheatsheet.events.OnCategoryClickEvent;
import com.dranithix.cheatsheet.events.OnNoteClickEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private final int NOTIFY_DELAY = 500;

    private List<Note> notes;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.image)
        ImageView coverArt;

        Note note;

        public void setNote(Note gear) {
            note = gear;
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new OnNoteClickEvent(note));
        }
    }

    public NoteListAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Note cat = notes.get(position);
        viewHolder.setNote(cat);

        Picasso.with(viewHolder.coverArt.getContext())
                .load(cat.getImagePath())
                .fit()
                .centerCrop()
                .into(viewHolder.coverArt);

        viewHolder.title.setText(cat.getTitle());
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

}