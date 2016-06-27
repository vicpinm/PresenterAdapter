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


public abstract class ViewHolderPresenter<T, H> {

    private static AtomicInteger keyGenerator = new AtomicInteger();

    private H mView;
    private T mItem;
    private Integer presenterId;

    public void setView(H view) {
        this.mView = view;
    }

    public void bind(T item) {
        this.mItem = item;
        onCreate();
    }

    public abstract void onCreate();

    public void onDestroy() {}

    public H getView(){
        return mView;
    }

    public T getData(){
        return mItem;
    }

    public int getPresenterId(){
        if(presenterId == null){
            presenterId = keyGenerator.getAndIncrement();
        }
        return presenterId;
    }
}
