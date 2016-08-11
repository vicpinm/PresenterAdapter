package com.vicpin.presenteradapter.sample.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.vicpin.presenteradapter.ViewHolder;
import com.vicpin.presenteradapter.ViewHolderPresenter;
import com.vicpin.presenteradapter.sample.R;
import com.vicpin.presenteradapter.sample.model.Country;
import com.vicpin.presenteradapter.sample.presenter.HeaderPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Victor on 25/06/2016.
 */
public class HeaderView extends ViewHolder<Country> implements HeaderPresenter.View {

    private HeaderPresenter mPresenter;

    @BindView(R.id.header)
    TextView mHeader;

    public HeaderView(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void createPresenter() {
        mPresenter = new HeaderPresenter();
    }

    @Override
    public ViewHolderPresenter getPresenter() {
        return mPresenter;
    }

    @Override public void setNumItems(int numItems) {
        mHeader.setText(getContext().getString(R.string.header, numItems));
    }
}
