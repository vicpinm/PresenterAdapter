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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Collections;
import java.util.List;

/**
 * Custom RecyclerView Adapter to hide MVP pattern implementation details
 * @param <T> data type for adapter
 */
public abstract class PresenterAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {

    private List<ViewInfo> registeredViewInfo = new ArrayList<>();
    private List<T> data = new ArrayList<>();

    /**
     * Header rows
     */
    private List<ViewInfo<T>> headers = new ArrayList<>();

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
    private boolean loadMoreInvoked;
    private final static int LOAD_MORE_TYPE = 99999;
    private final static int HEADER_TYPE = 100000;
    private static int HEADER_MAX_TYPE = HEADER_TYPE;
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
        if(isHeaderType(viewType)){
            return headers.get(viewType - HEADER_TYPE);
        }
        else {
            return registeredViewInfo.get(viewType);
        }
    }

    public boolean isHeaderType(int viewType){
        return viewType >= HEADER_TYPE && viewType < HEADER_MAX_TYPE;
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
        else if(isHeaderPosition(position)){
            return HEADER_TYPE + position;
        }

        ViewInfo viewInfo = getViewInfo(position);
        return getTypeForViewHolder(viewInfo);
    }

    private boolean isLoadMorePosition(int position){
        return loadMoreEnabled && getItemCount() - 1 == position;
    }

    private boolean isHeaderPosition(int position){
        return position < headers.size();
    }

    private boolean isNormalPosition(int position){
        return !isLoadMorePosition(position) && !isHeaderPosition(position);
    }

    private int getTypeForViewHolder(ViewInfo viewInfo) {
        if(!registeredViewInfo.contains(viewInfo)){
            registeredViewInfo.add(viewInfo);
        }
        return registeredViewInfo.indexOf(viewInfo);
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        if(isNormalPosition(position)) {
            holder.onBind(data, getPositionWithoutHeaders(position));
            appendListeners(holder);
        }
        else if(isHeaderPosition(position)){
            holder.onBindHeader(data);
        }
        else if(isLoadMorePosition(position)){
            notifyLoadMoreReached();
        }
    }

    public int getPositionWithoutHeaders(int position){
        return position - headers.size();
    }

    public int getPositionWithHeaders(int position){
        return position + headers.size();
    }

    private void notifyLoadMoreReached() {
        if(loadMoreListener != null && !loadMoreInvoked){
            loadMoreInvoked = true;
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
    }

    @Override public boolean onFailedToRecycleView(ViewHolder<T> holder) {
        holder.onDestroy();
        return super.onFailedToRecycleView(holder);
    }

    public T getItem(int position){
        return data.get(getPositionWithoutHeaders(position));
    }

    public void addHeader(ViewInfo<T> headerInfo){
        this.headers.add(headerInfo);
        HEADER_MAX_TYPE = HEADER_TYPE + headers.size();
    }

    public void updateHeaders(){
        if(this.headers != null && this.headers.size() > 0){
            notifyItemRangeChanged(0, headers.size());
        }
    }

    /**
     * Set adapter data and notifies the change
     * @param data items collection
     * @return PresenterAdapter called instance
     */
    public PresenterAdapter<T> setData(@NonNull List<T> data){
        this.data = data;
        this.loadMoreInvoked = false;
        notifyDataSetChanged();
        return this;
    }

    /**
     * Set adapter data and notifies the change, keeping scroll position
     * @param data items collection
     * @param recyclerView RecyclerView instance
     * @return PresenterAdapter called instance
     */
    public PresenterAdapter<T> setDataKeepScroll(@NonNull List<T> data,@NonNull RecyclerView recyclerView){
        this.data = data;
        this.loadMoreInvoked = false;
        int currentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop());

        notifyDataSetChanged();

        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(currentPosition, top);

        return this;
    }

    public List<T> getData(){
        return this.data;
    }

    @Override public int getItemCount() {
        return (data != null ? data.size() : 0) + (headers != null ? headers.size() : 0) + (loadMoreEnabled ? 1 : 0);
    }

    public int getHeadersCount(){
        return headers != null ? headers.size() : 0;
    }

    /**
     * Add data at the end of the current data list and notifies the change
     * @param data items collection to append at the end of the current collection
     * @return PresenterAdapter called instance
     */
    public void addData(@NonNull List<T> data){

        this.loadMoreInvoked = false;

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

    public void removeItem(T item){
        if(this.data != null && this.data.contains(item)){
            notifyItemRemoved(getPositionWithHeaders(this.data.indexOf(item)));
            this.data.remove(item);
        }
    }

    /**
     * Swap two no header items
     * @param from
     * @param to
     */
    public void swapItems(int from, int to){
        if(isHeaderPosition(from) || isHeaderPosition(to)){
            throw new IllegalArgumentException("Header positions are not swapable");
        }

        from -= getHeadersCount();
        to -= getHeadersCount();

        if(from >= getData().size() || to >= getData().size()){
            throw new IllegalArgumentException("Cannot swap items, data size is " + data.size());
        }

        if(from == to){
            return;
        }

        Collections.swap(getData(),from, to);

        notifyItemMoved(from, to);

    }

    /**
     * Move one item to another position, updating intermediates positions
     * @param from
     * @param to
     */
    public void moveItem(int from, int to){
        if(isHeaderPosition(from) || isHeaderPosition(to)){
            throw new IllegalArgumentException("Header positions are not swapable");
        }

        from -= getHeadersCount();
        to -= getHeadersCount();

        if(from >= getData().size() || to >= getData().size()){
            throw new IllegalArgumentException("Cannot move item, data size is " + data.size());
        }

        if(from == to){
            return;
        }

        T item = getData().remove(from);
        getData().add(to, item);

        notifyItemMoved(from, to);

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
        this.loadMoreInvoked = false;
        this.loadMoreListener = loadMoreListener;
        notifyItemInserted(getItemCount());
    }

    /**
     * Disable load more option
     */
    public void disableLoadMore(){
        PresenterAdapter.this.loadMoreEnabled = false;
        PresenterAdapter.this.loadMoreInvoked = false;
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

    /**
     * Reload current views showing on the screen
     * @param recyclerView
     */
    public void refreshViews(RecyclerView recyclerView){
        if(recyclerView.getLayoutManager() != null) {
            int firstPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            for (int i = firstPosition; i <= lastPosition; i++) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        if(hasStableIds()) {
            return position < getHeadersCount() ? headers.get(position).hashCode() : getItem(position).hashCode();
        }
        else{
            return super.getItemId(position);
        }
    }
}
