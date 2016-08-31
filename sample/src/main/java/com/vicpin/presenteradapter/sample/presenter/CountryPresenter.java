package com.vicpin.presenteradapter.sample.presenter;

import com.vicpin.presenteradapter.ViewHolderPresenter;
import com.vicpin.presenteradapter.sample.model.Country;

/**
 * Created by Victor on 25/06/2016.
 */
public class CountryPresenter extends ViewHolderPresenter<Country, CountryPresenter.View> {

    @Override
    public void onCreate() {
        setCountryName();
        setPresenterId();
        setCountryImage();
    }

    public void setCountryName(){
        getView().setCountryName(getData().getName());
    }

    public void setPresenterId(){
        getView().setInfo("Rendered with presenter #" + getPresenterId());
    }

    @Override public void onDestroy() {
        getView().notifyPresenterDetroyed(getPresenterId());
    }

    public void setCountryImage(){
        getView().setImage(getData().getImageResourceId());
    }

    public void onDeleteItem() {
        getView().deleteItem(getData());
    }

    public interface View {
        void setCountryName(String s);

        void setInfo(String s);

        void setImage(int resourceId);

        void notifyPresenterDetroyed(int presenterId);

        void deleteItem(Country data);
    }
}
