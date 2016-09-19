/*
 * Copyright 2016 Victor Manuel Pineda Murcia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vicpin.presenteradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * ViewHolder parent class
 * @param <T> Adapter data type
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

    private Context mContext;
    private Object customListener;

    public ViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
    }

    protected void setCustomLister(Object customListener){
        this.customListener = customListener;
    }

    /**
     * Get custom listener instance from PresenterAdapter instance
     * @return
     */
    public Object getCustomListener(){
        return customListener;
    }

    /**
     * Called when adapter's onBindViewHolder is executed for a normal row type
     * Initializes presenter binding view and data item
     */
    public void onBind(List<T> data, int position) {
        setupPresenter(data);

        if(getPresenter() != null) {
            getPresenter().bind(data.get(position));
            getPresenter().onCreate();
        }
    }

    /**
     * Called when adapter's onBindViewHolder is executed for a header row type
     * Initializes presenter binding view but no data item
     */
    public void onBindHeader(List<T> data) {
        setupPresenter(data);

        if(getPresenter() != null) {
            getPresenter().onCreate();
        }
    }

    /**
     * Initializes presenter binding view and adapter data collection
     * @param data adapter data collection
     */
    private void setupPresenter(List<T> data){
        createPresenter();

        if(getPresenter() != null) {
            getPresenter().setView(this);
            getPresenter().bindDataCollection(data);
        }
    }

    /**
     * Called first time presenter is created for each ViewHolder in the adapter
     */
    public abstract void createPresenter();

    public abstract ViewHolderPresenter getPresenter();

    public Context getContext(){
        return mContext;
    }

    /**
     * Called when view is recycled in the adapter
     */
    public void onDestroy(){
        if(getPresenter() != null) {
            getPresenter().onDestroy();
        }
    }
}
