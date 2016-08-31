package com.vicpin.presenteradapter.sample.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vicpin.presenteradapter.ViewHolder;
import com.vicpin.presenteradapter.ViewHolderPresenter;
import com.vicpin.presenteradapter.sample.R;
import com.vicpin.presenteradapter.sample.model.Country;
import com.vicpin.presenteradapter.sample.presenter.CountryPresenter;
import com.vicpin.presenteradapter.sample.view.interfaces.ItemDeletedListener;
import com.vicpin.presenteradapter.sample.view.interfaces.ItemRecycledListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Victor on 25/06/2016.
 */
public class CountryView extends ViewHolder<Country> implements CountryPresenter.View {

    private CountryPresenter mPresenter;

    @BindView(R.id.countryName)
    TextView mCountryName;

    @BindView(R.id.textInfo)
    TextView mTextInfo;

    @BindView(R.id.imageView)
    ImageView mImageView;

    public CountryView(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void createPresenter() {
        mPresenter = new CountryPresenter();
    }

    @Override
    public ViewHolderPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setCountryName(String text) {
        mCountryName.setText(text);
    }

    @Override
    public void setInfo(String info) {
        mTextInfo.setText(info);
    }

    @Override
    public void setImage(int resourceId) {
        mImageView.setImageResource(resourceId);
    }

    /**
     * Example of activity/fragment comunication through custom listener instance
     * @param presenterId
     */
    @Override public void notifyPresenterDetroyed(int presenterId) {
        if(getCustomListener() != null) {
            ((ItemRecycledListener) getCustomListener()).onItemRecycled(presenterId);
        }
    }

    @OnClick(R.id.deleteButton)
    public void onDeleteButtonClicked(View v){
        mPresenter.onDeleteItem();
    }

    public void deleteItem(Country item){
        ((ItemDeletedListener) getCustomListener()).onItemDeleted(item);
    }
}
