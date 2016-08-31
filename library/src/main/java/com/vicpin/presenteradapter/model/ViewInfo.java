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
package com.vicpin.presenteradapter.model;

import android.support.annotation.LayoutRes;

import com.vicpin.presenteradapter.ViewHolder;

/**
 * Data structure which represents a ViewHolder and layout resource association
 * @param <T> data type for adapter
 */
public class ViewInfo<T> {

    private Class<? extends ViewHolder<T>> viewHolderClass;

    private int viewResourceId;

    private ViewInfo(){}

    /**
     * Creates a new ViewInfo instance with a concrete ViewHolder class
     * @param viewHolderClass class that inherit from ViewHolder class and implements a view for a concrete type of cell in the adapter
     * @param <T> data type for adapter
     * @return new ViewInfo instance
     */
    public static <T> ViewInfo<T> with(Class<? extends ViewHolder<T>> viewHolderClass) {
        ViewInfo<T> viewInfo = new ViewInfo<>();
        viewInfo.setViewHolderClass(viewHolderClass);
        return viewInfo;
    }

    /**
     * Specifies the layout used int the ViewHolder class
     * @param layoutResourceId
     * @return
     */
    public ViewInfo<T> setLayout(@LayoutRes int layoutResourceId){
        this.viewResourceId = layoutResourceId;
        return this;
    }

    public Class<? extends ViewHolder<T>> getViewHolderClass() {
        return viewHolderClass;
    }

    public int getViewResourceId() {
        return viewResourceId;
    }

    private void setViewHolderClass(Class<? extends ViewHolder<T>> viewHolderClass) {
        this.viewHolderClass = viewHolderClass;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewInfo<?> viewInfo = (ViewInfo<?>) o;

        if (viewResourceId != viewInfo.viewResourceId) return false;
        return viewHolderClass != null ? viewHolderClass.equals(viewInfo.viewHolderClass) : viewInfo.viewHolderClass == null;

    }

    @Override public int hashCode() {
        int result = viewHolderClass != null ? viewHolderClass.hashCode() : 0;
        result = 31 * result + viewResourceId;
        return result;
    }
}
