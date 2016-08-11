package com.vicpin.presenteradapter.sample.presenter;

import com.vicpin.presenteradapter.ViewHolderPresenter;
import com.vicpin.presenteradapter.sample.model.Country;

/**
 * Created by Victor on 25/06/2016.
 */
public class HeaderPresenter extends ViewHolderPresenter<Country, HeaderPresenter.View> {

    @Override
    public void onCreate() {
        showNumItems();
    }

    public void showNumItems(){
        getView().setNumItems(getDataCollection().size());
    }

    public interface View {
        void setNumItems(int numItems);
    }
}
