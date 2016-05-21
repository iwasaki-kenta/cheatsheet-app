package com.dranithix.cheatsheet.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/22/2016.
 */

public class Note implements Parcelable {
    private String id;
    private String title;
    private String categoryId;
    private List<String> tags = new ArrayList<String>();
    private String data;

    private ParseObject uploadedBy;

    public Note() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ParseObject getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(ParseObject uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        categoryId = in.readString();
        if (in.readByte() == 0x01) {
            tags = new ArrayList<String>();
            in.readList(tags, String.class.getClassLoader());
        } else {
            tags = null;
        }
        data = in.readString();
        uploadedBy = (ParseObject) in.readValue(ParseObject.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(categoryId);
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
        dest.writeString(data);
        dest.writeValue(uploadedBy);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}