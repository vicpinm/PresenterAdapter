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

public class SimplePresenterAdapter<T> extends PresenterAdapter<T> {

    private Class<? extends ViewHolder<T>> viewHolderClass;
    private int layoutResourceId;

    private SimplePresenterAdapter(){}

    public static <T> SimplePresenterAdapter<T> with(Class<? extends ViewHolder<T>> viewHolderClass){
        SimplePresenterAdapter<T> adapter = new SimplePresenterAdapter<>();
        adapter.viewHolderClass = viewHolderClass;
        return adapter;
    }

    public SimplePresenterAdapter setLayout(int layoutResourceId){
        this.layoutResourceId = layoutResourceId;
        return this;
    }

    @Override public ViewInfo getViewInfo(int position) {
        return ViewInfo.createView(viewHolderClass).withLayout(layoutResourceId);
    }


}
