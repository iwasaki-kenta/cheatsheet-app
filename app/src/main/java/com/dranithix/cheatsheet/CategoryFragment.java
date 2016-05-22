package com.dranithix.cheatsheet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dranithix.cheatsheet.entities.Category;
import com.dranithix.cheatsheet.entities.Subcategory;
import com.dranithix.cheatsheet.ui.CategoryListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment {
    private CategoryListAdapter adapter;
    private List<Category> categories = new ArrayList<Category>();

    @Bind(R.id.list)
    RecyclerView list;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ButterKnife.bind(this, view);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter = new CategoryListAdapter(categories));

        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Notes");

        if (categories.size() == 0) {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle("Loading...");
            dialog.setMessage("Finding subjects online...");
            dialog.show();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
            query.orderByAscending("name");
            query.whereEqualTo("parentId", "-1");

            final String[] imageUrls = new String[]{
                    "http://www.nyas.org/image.axd?id=17556476-8691-4967-b6d6-bedfb8f9c5aa&t=634856441733570000",
                    "http://www.filcatholic.org/wp-content/uploads/2015/09/international-mother-language-day_21213.jpg",
                    "http://oolwebsites.s3.amazonaws.com/wp-content/uploads/books-english-literature-igcse.jpg",
                    "http://www.thegreatcourses.com/media/catalog/product/cache/1/image/800x600/0f396e8a55728e79b48334e699243c07/1/7/1761---base_image.1424269355.jpg",
                    "http://www.bu.edu/math/files/2010/06/golden-ratio.jpg",
                    "http://www.cmu.edu/physics/news/2016/images/gravwaves_large.jpg"};

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> cat, ParseException e) {
                    if (e == null) {
                        int iterate = 0;
                        for (ParseObject obj : cat) {
                            Category category = new Category();
                            category.setId(obj.getObjectId());
                            category.setName(obj.getString("name"));
                            category.setImagePath(imageUrls[iterate++]);
                            category.setCount(obj.getInt("count"));
                            System.out.println(category.getName());
                            categories.add(category);
                        }
                        adapter.notifyItemRangeInserted(0, cat.size());
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
