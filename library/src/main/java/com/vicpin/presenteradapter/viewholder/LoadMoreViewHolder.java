package com.vicpin.presenteradapter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.vicpin.presenteradapter.ViewHolder;
import com.vicpin.presenteradapter.ViewHolderPresenter;

/**
 * Created by Oesia on 02/08/2016.
 */
public class LoadMoreViewHolder extends ViewHolder {

    private LoadMoreViewHolder(View itemView) {
        super(itemView);
    }

    public static LoadMoreViewHolder newInstance(Context context, int layout){
        View v = LayoutInflater.from(context).inflate(layout, null, false);
        return new LoadMoreViewHolder(v);
    }

    @Override public void createPresenter() {

    }

    @Override public ViewHolderPresenter getPresenter() {
        return null;
    }
}
