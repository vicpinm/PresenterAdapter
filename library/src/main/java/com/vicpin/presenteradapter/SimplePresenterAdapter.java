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

import com.vicpin.presenteradapter.model.ViewInfo;

/**
 * Custom PresenterAdapter for single item type adapters.
 * No need to implement any adapter any method, only instanciate adapter and set to recyclerView.
 * Instanciate with the static factory method "with", and set layout resource with "seLayout"
 *
 * @param <T> adapter item type
 */
public class SimplePresenterAdapter<T> extends PresenterAdapter<T> {

    private Class<? extends ViewHolder<T>> viewHolderClass;
    private int layoutResourceId;

    private SimplePresenterAdapter(){}

    /**
     * Static factory method to create new SimplePresenterAdapter instance. Layout resource must be setted with setLayout method.
     * @param viewHolderClass ViewHolder class used to build the view
     * @param <T> adapter data type
     * @return new SimplePresenterAdapter instance
     */
    public static <T> SimplePresenterAdapter<T> with(Class<? extends ViewHolder<T>> viewHolderClass){
        SimplePresenterAdapter<T> adapter = new SimplePresenterAdapter<>();
        adapter.viewHolderClass = viewHolderClass;
        return adapter;
    }

    /**
     * Layout setter used by the adapter to build the view
     * @param layoutResourceId
     * @return
     */
    public SimplePresenterAdapter setLayout(int layoutResourceId){
        this.layoutResourceId = layoutResourceId;
        return this;
    }

    @Override public ViewInfo getViewInfo(int position) {
        return ViewInfo.with(viewHolderClass).setLayout(layoutResourceId);
    }


}
