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
import com.dranithix.cheatsheet.events.OnCategoryClickEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private final int NOTIFY_DELAY = 500;

    private List<Category> categories;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.image)
        ImageView coverArt;
        @Bind(R.id.count)
        TextView count;

        Category category;

        public void setCategory(Category gear) {
            this.category = gear;
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new OnCategoryClickEvent(category));
        }
    }

    public CategoryListAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Category cat = categories.get(position);
        viewHolder.setCategory(cat);
        viewHolder.count.setText(String.valueOf(cat.getCount()) + " Note(s)");

        Picasso.with(viewHolder.coverArt.getContext())
                .load(cat.getImagePath())
                .fit()
                .centerCrop()
                .into(viewHolder.coverArt);

        viewHolder.title.setText(cat.getName());
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addGear(final Category gear, final int position) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                categories.add(position, gear);
                notifyItemInserted(position);
            }
        }, NOTIFY_DELAY);
    }

    public void removeGear(final int position) {
        categories.remove(position);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(position);
            }
        }, NOTIFY_DELAY);
    }

    // endregion
}