package com.dranithix.cheatsheet.ui;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dranithix.cheatsheet.R;
import com.dranithix.cheatsheet.entities.Note;
import com.dranithix.cheatsheet.entities.Question;
import com.dranithix.cheatsheet.events.OnNoteClickEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {
    private final int NOTIFY_DELAY = 500;

    private List<Question> questions;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @Bind(R.id.question)
        ImageView title;

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

    public QuestionListAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_math, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Question cat = questions.get(position);

        Picasso.with(viewHolder.title.getContext())
                .load("http://latex.codecogs.com/gif.latex?" + Uri.encode(cat.getQuestion()))
                .fit()
                .centerInside()
                .into(viewHolder.title);
    }

    @Override
    public long getItemId(int position) {
        return questions.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

}