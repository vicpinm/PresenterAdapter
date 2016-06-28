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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class parent for Presenters asociated with ViewHolder classes
 * @param <Data> Adapter data type
 * @param <PresenterView> Interface View asociated with the ViewHolder's presenter
 */
public abstract class ViewHolderPresenter<Data, PresenterView> {

    /**
     * Incremental integer generator for presenter
     */
    private static AtomicInteger presenterIdsGenerator = new AtomicInteger();

    private PresenterView mView;
    private Data mItem;
    private Integer presenterId;

    public void setView(PresenterView view) {
        this.mView = view;
    }

    public void bind(Data item) {
        this.mItem = item;
        onCreate();
    }

    /**
     * Called when the view becomes visible in the adapter
     */
    public abstract void onCreate();

    /**
     * Called when the view is recycled and is no more visible in the adapter
     */
    public void onDestroy() {}

    public PresenterView getView(){
        return mView;
    }

    public Data getData(){
        return mItem;
    }

    public int getPresenterId(){
        if(presenterId == null){
            presenterId = presenterIdsGenerator.getAndIncrement();
        }
        return presenterId;
    }
}
