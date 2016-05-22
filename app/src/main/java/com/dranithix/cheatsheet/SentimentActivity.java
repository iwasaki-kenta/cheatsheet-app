package com.dranithix.cheatsheet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dranithix.cheatsheet.entities.Question;
import com.dranithix.cheatsheet.ui.QuestionListAdapter;
import com.dranithix.cheatsheet.util.DebugUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

public class SentimentActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView list;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.description)
    TextView description;

    List<Question> questions = new ArrayList<Question>();

    QuestionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Multi-dimensional Integrals");

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter = new QuestionListAdapter(questions));
    }

    boolean toggle = true;

    @OnLongClick(R.id.description)
    public boolean extractInfo() {
        if (toggle) {
            questions.clear();
            questions.addAll(DebugUtil.testQuestionData());
            adapter.notifyItemRangeInserted(0, questions.size());
            toggle = false;
            description.setText("In a similar way, we will introduce two new coordinate systems in three-dimensional space: cylindrical coordinates and spherical coordinates that greatly simplify the computation of triple integrals over certain commonly occurring solid regions.In much the same way that our attempt to solve the area problem led to the definition of a definite integral, we now seek to find the volume of a solid and in the process we arrive at the definition of a double integral.");
        } else {
            adapter.notifyItemRangeRemoved(0, questions.size());
            questions.clear();
            description.setText("");
            description.setText("In this chapter we extend the idea of a definite integral to double and triple integrals of functions of two or three variables. These ideas are then used to compute volumes, masses, and centroids of more general regions than we were able to consider in Chapters 6 and 8. We also use double integrals to calculate probabilities when two random variables are involved. We will see that polar coordinates are useful in computing double integrals over some types of regions. In a similar way, we will introduce two new coordinate systems in three-dimensional space––cylindrical coordinates and spherical coordinates––that greatly simplify the computation of triple integrals over certain commonly occurring solid regions.In much the same way that our attempt to solve the area problem led to the definition of a definite integral, we now seek to find the volume of a solid and in the process we arrive at the definition of a double integral.");
            toggle = true;
        }

        return true;
    }
}
