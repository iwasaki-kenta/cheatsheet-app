package com.dranithix.cheatsheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dranithix.cheatsheet.entities.Note;
import com.dranithix.cheatsheet.entities.Subcategory;
import com.dranithix.cheatsheet.events.OnNoteClickEvent;
import com.dranithix.cheatsheet.ui.NoteListAdapter;
import com.dranithix.cheatsheet.util.DebugUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customfonts.MyTextView;

public class NoteListActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView list;

    MyTextView toolbarTitle;

    NoteListAdapter adapter;

    List<Note> notes = new ArrayList<Note>();

    Subcategory subcategory;

    @OnClick(R.id.back_button)
    public void backButton() {
        finish();
    }

    @OnClick(R.id.add_note)
    public void addNote() {
        final ParseObject note = new ParseObject("Notes");
        note.put("title", "Untitled");
        note.put("categoryId", subcategory.getId());
        note.put("tags", new ArrayList<String>());

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Creating...");
        dialog.setMessage("Reserving space for your note...");
        dialog.show();

        note.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();

                Intent i = new Intent(NoteListActivity.this, DrawActivity.class);
                i.putExtra("note", new Note(note));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list_fragment);
        toolbarTitle = (MyTextView) findViewById(R.id.toolbar_title);

        ButterKnife.bind(this);

        Bundle params = getIntent().getExtras();

        if (params != null) {
            subcategory = (Subcategory) getIntent().getParcelableExtra("subcategory");
        } else {
            finish();
            return;
        }

        list.setLayoutManager(new GridLayoutManager(this, 2));
        list.setAdapter(adapter = new NoteListAdapter(notes));



    }

    @Override
    protected void onResume()
    {
        super.onResume();

        notes.clear();

        toolbarTitle.setText(subcategory.getName());

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Fetching notes...");
        dialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notes");
        query.orderByAscending("title");
        query.whereEqualTo("categoryId", subcategory.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> cat, ParseException e) {
                if (e == null) {
//                    int i = 0;
//                    for (ParseObject obj : cat) {
//                        Note note = new Note(obj);
//                        notes.add(note);
//                    }
                    notes.addAll(DebugUtil.testNoteData());
                    adapter.notifyItemRangeInserted(0, notes.size());
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
                dialog.dismiss();
            }
        });
    }

    @Subscribe
    public void noteClicked(OnNoteClickEvent event) {
        Note note = event.note;
        Intent i = new Intent(NoteListActivity.this, DrawActivity.class);
        i.putExtra("note", note);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
