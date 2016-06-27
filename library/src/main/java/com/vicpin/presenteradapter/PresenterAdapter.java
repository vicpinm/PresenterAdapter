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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vicpin.presenteradapter.model.ViewInfo;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class PresenterAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {

    private List<ViewInfo> registeredViewInfo = new ArrayList<>();
    private List<T> data = new ArrayList<>();

    public PresenterAdapter() {
    }

    public PresenterAdapter(List<T> data) {
        this.data = data;
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewInfo viewInfo = getViewInfoForType(viewType);
        return getViewInfo(parent, viewInfo);
    }

    private ViewInfo getViewInfoForType(int viewType){
        return registeredViewInfo.get(viewType);
    }

    private ViewHolder<T> getViewInfo(ViewGroup parent, ViewInfo viewInfo) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewInfo.getViewResourceId(), parent, false);
            Constructor<ViewHolder<T>> constructor =  viewInfo.getViewHolderClass().getConstructor(View.class);
            return constructor.newInstance(view);
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override public int getItemViewType(int position) {
        ViewInfo viewInfo = getViewInfo(position);
        return getTypeForViewHolder(viewInfo);
    }

    private int getTypeForViewHolder(ViewInfo viewInfo) {
        if(!registeredViewInfo.contains(viewInfo)){
            registeredViewInfo.add(viewInfo);
        }
        return registeredViewInfo.indexOf(viewInfo);
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }

    @Override public void onViewRecycled(ViewHolder<T> holder) {
        super.onViewRecycled(holder);
        holder.onDestroy();
    }

    @Override public boolean onFailedToRecycleView(ViewHolder<T> holder) {
        holder.onDestroy();
        return super.onFailedToRecycleView(holder);
    }

    public T getItem(int position){
        return data.get(position);
    }

    public PresenterAdapter<T> setData(List<T> data){
        this.data = data;
        notifyDataSetChanged();
        return this;
    }

    public void addData(List<T> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override public int getItemCount() {
        return data.size();
    }

    public abstract ViewInfo getViewInfo(int position);
}
