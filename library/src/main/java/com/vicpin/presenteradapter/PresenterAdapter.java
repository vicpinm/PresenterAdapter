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

import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vicpin.presenteradapter.listeners.ItemClickListener;
import com.vicpin.presenteradapter.listeners.ItemLongClickListener;
import com.vicpin.presenteradapter.listeners.OnLoadMoreListener;
import com.vicpin.presenteradapter.model.ViewInfo;
import com.vicpin.presenteradapter.viewholder.LoadMoreViewHolder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom RecyclerView Adapter to hide MVP pattern implementation details
 * @param <T> data type for adapter
 */
public abstract class PresenterAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {

    private List<ViewInfo> registeredViewInfo = new ArrayList<>();
    private List<T> data = new ArrayList<>();

    /**
     * Event listeners
     */
    private ItemClickListener<T> itemClickListener;
    private ItemLongClickListener<T> itemLongClickListener;
    private OnLoadMoreListener loadMoreListener;
    private Object customListener;

    /**
     * Load more properties
     */
    private boolean loadMoreEnabled;
    private final static int LOAD_MORE_TYPE = 99999;
    private int loadMoreLayout = R.layout.adapter_load_more;


    public PresenterAdapter() {
    }

    public PresenterAdapter(List<T> data) {
        this.data = data;
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == LOAD_MORE_TYPE){
            return LoadMoreViewHolder.newInstance(parent.getContext(), loadMoreLayout);
        }
        else {
            ViewInfo viewInfo = getViewInfoForType(viewType);
            return getViewHolder(parent, viewInfo);
        }
    }

    private ViewInfo getViewInfoForType(int viewType){
        return registeredViewInfo.get(viewType);
    }

    private ViewHolder<T> getViewHolder(ViewGroup parent, ViewInfo viewInfo) {
        try {
            View view = createView(viewInfo.getViewResourceId(), parent);
            Constructor<ViewHolder<T>> constructor =  viewInfo.getViewHolderClass().getConstructor(View.class);
            ViewHolder<T> viewHolder = constructor.newInstance(view);
            viewHolder.setCustomLister(customListener);
            return viewHolder;
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private View createView(@LayoutRes int layoutResourceId, ViewGroup parentView){
        return LayoutInflater.from(parentView.getContext()).inflate(layoutResourceId, parentView, false);
    }

    @Override public int getItemViewType(int position) {
        if(isLoadMorePosition(position)){
            return LOAD_MORE_TYPE;
        }
        ViewInfo viewInfo = getViewInfo(position);
        return getTypeForViewHolder(viewInfo);
    }

    private boolean isLoadMorePosition(int position){
        return loadMoreEnabled && data.size() == position;
    }

    private int getTypeForViewHolder(ViewInfo viewInfo) {
        if(!registeredViewInfo.contains(viewInfo)){
            registeredViewInfo.add(viewInfo);
        }
        return registeredViewInfo.indexOf(viewInfo);
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        if(!isLoadMorePosition(position)) {
            holder.onBind(getItem(position));
            appendListeners(holder);
        }
        else if(loadMoreEnabled){
            notifyLoadMoreReached();
        }
    }

    private void notifyLoadMoreReached() {
        if(loadMoreListener != null){
            loadMoreListener.onLoadMore();
        }
    }

    private void appendListeners(final ViewHolder<T> viewHolder){
        if(itemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    itemClickListener.onItemClick(getItem(viewHolder.getAdapterPosition()), viewHolder);
                }
            });
        }

        if(itemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    itemLongClickListener.onItemLongClick(getItem(viewHolder.getAdapterPosition()), viewHolder);
                    return true;
                }
            });
        }
    }

    @Override public void onViewRecycled(ViewHolder<T> holder) {
        super.onViewRecycled(holder);
        holder.onDestroy();
        Log.e("DESTROY", "position" + holder.getAdapterPosition());
    }

    @Override public boolean onFailedToRecycleView(ViewHolder<T> holder) {
        holder.onDestroy();
        return super.onFailedToRecycleView(holder);
    }

    public T getItem(int position){
        return data.get(position);
    }

    /**
     * Set adapter data and notifies the change
     * @param data items collection
     * @return PresenterAdapter called instance
     */
    public PresenterAdapter<T> setData(@NonNull List<T> data){
        this.data = data;
        notifyDataSetChanged();
        return this;
    }

    @Override public int getItemCount() {
        return data.size() + (loadMoreEnabled ? 1 : 0);
    }

    /**
     * Add data at the end of the current data list and notifies the change
     * @param data items collection to append at the end of the current collection
     * @return PresenterAdapter called instance
     */
    public void addData(@NonNull List<T> data){
        final int currentItemCount = getItemCount();
        this.data.addAll(data);
        final int dataSize = data.size();

        new Handler().post(new Runnable() {
            @Override public void run() {
                if(loadMoreEnabled) {
                    notifyItemChanged(currentItemCount - 1);
                }
                else{
                    notifyItemRangeInserted(currentItemCount, dataSize);
                }

            }
        });
    }

    /**
     * Clear adapter data and notifies the change
     */
    public void clearData(){
        this.data = new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * Called for each adapter position to get the associated ViewInfo object.
     * ViewInfo class holds the ViewHolder class and the associated layout for building the view
     * @param position item position in the adapter items collection
     * @return new instance of ViewInfo object
     */
    public abstract ViewInfo getViewInfo(int position);

    /**
     * Setter method for click event listener.
     * Called when user clicks on any cell in recyclerview
     * @param clickListener click listener
     */
    public void setItemClickListener(ItemClickListener<T> clickListener){
        this.itemClickListener = clickListener;
    }

    /**
     * Setter method for long click event listener.
     * Called when user performs a long press on any cell in recyclerview
     * @param longClickListener long click listener
     */
    public void setItemLongClickListener(ItemLongClickListener<T> longClickListener){
        this.itemLongClickListener = longClickListener;
    }

    /**
     * Sets a custom listener instance. You can call to the listener from your ViewHolder classes with getCustomListener() method.
     * @param customListener
     */
    public void setCustomListener(Object customListener){
        this.customListener = customListener;
    }


    /**
     * Enable load more option for paginated collections
     * @param loadMoreListener
     */
    public void enableLoadMore(@Nullable OnLoadMoreListener loadMoreListener) {
        this.loadMoreEnabled = true;
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * Disable load more option
     */
    public void disableLoadMore(){
        this.loadMoreEnabled = false;
        notifyItemRemoved(getItemCount());
    }

    public boolean isLoadMoreEnabled() {
        return loadMoreEnabled;
    }

    /**
     * Set custom load more resource layout
     * @param layoutResource
     */
    public void setLoadMoreLayout(@LayoutRes int layoutResource){
        this.loadMoreLayout = layoutResource;
    }
}
