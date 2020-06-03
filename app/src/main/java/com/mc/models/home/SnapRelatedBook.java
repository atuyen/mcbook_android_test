package com.mc.models.home;

import java.util.List;

public class SnapRelatedBook {
    private int mGravity;
    private String mCategoryName;
    private List<RelatedBook> mApps;

    public SnapRelatedBook(int gravity, String categoryName, List<RelatedBook> apps) {
        mGravity = gravity;
        mCategoryName = categoryName;
        mApps = apps;
    }

    public String getCategoryName(){
        return mCategoryName;
    }

    public int getGravity(){
        return mGravity;
    }

    public List<RelatedBook> getApps(){
        return mApps;
    }
}
