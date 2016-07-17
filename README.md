# PresenterAdapter

A lighweight Android library to implement adapters for your RecyclerViews in a clean way, using the MVP pattern.

## Features

  * Avoid writing adapter classes, you only has to focus in view clases and their presenters, following the MVP pattern.
  * View representation and view logic decoupled from adapter framework classes.
  * Easy creation of different kinds of views for the same RecyclerView.
  * Avoid create new presenters for each row, presenter instances are recycled in the same way that adapter does with ViewHolder clases.
  * Lifecycle callbacks in presenter clases. You can control view creation and destroy for each RecyclerView position. Presenters are notified when they are destroyed to perform clear and unsubscribe operations if needed.
  * Custom presenter creation. You are responsible for creating presenter instance the same way yo usually do in your Activities or Fragments, which allows you to use tools like Dagger to inject your dependencies (see description below for details).

<p align="center">
  <img src ="/uml_diagram.png" />
</p>


## Usage
### Single view type adapter

For adapters with a unique kind of view, there is no need to create any adapter class. SimplePresenterAdapter is provided for this kind of lists.
 
##### Adapter creation sample for a list of countries. 
  Extracted from the sample, CountryView.java is the class which implements the view layer for each adapter position, the same way Activities or Fragment does.
  Data setter is optional and can be setted in any moment later.
             
        PresenterAdapter<Country> adapter = SimplePresenterAdapter.with(CountryView.class)
        .setLayout(R.layout.adapter_country)
        .setData(data);

### View class

Your view class inherits from ViewHolder<Data> class. This class is responsible for implementing the view layer in MPV pattern, equivalent to Activities or Fragments, and creating a presenter instance.

    public class CountryView extends ViewHolder<Country> implements CountryPresenter.View {

    private CountryPresenter mPresenter;

    @BindView(R.id.countryName)
    TextView mCountryName;
    
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

}

### Presenter class

Class responsible for implementing the presenter layer in MPV pattern, equivalent to any other presenter. It inherits from ViewHolderPresenter<Data, PresenterView> class. 
This class is generic and you need to indicate two types, your adapter data type (<Country> in the sample) and your presenter view interface. 
You have to override onCreate method to setup your view. Also, you can override onDestroy method if you need to implement any destroy logic for your view.
Inside your presenter class, you have access to getData() method, in order to get the current data instance for the view, obtained from the adapter data collection.
Also, inside your presenter class, you have access to getView() method, in order to interact with your view class.

    public class CountryPresenter extends ViewHolderPresenter<Country, CountryPresenter.View> {

        @Override
        public void onCreate() {
            setCountryName();
        }

        public void setCountryName(){
            getView().setCountryName(getData().getName());
        }

        public interface View {
             void setCountryName(String s);
        }
    }

### Multiple view type adapter

It is very easy to implement multiple view types in for your RecyclerView. Instead of use SimpleAdapterPresenter, you have to implement your own adapter, in order to implement your representation logic. Your adapter class must extends from PresenterAdapter class.
PresenterAdapter class has only one abstract method you have to implement, getViewInfo(int position) method. This method returns an instance of ViewInfo class, which holds an association between your view class and your layour resource for a given position.


##### Example of different types of views based on item position, using the same view class and differents layouts:

    public class MultipleAdapter extends PresenterAdapter<Country> {

    @Override public ViewInfo getViewInfo(int position) {
        if(position % 2 == 0)
            return ViewInfo.setView(CountryView.class).withLayout(R.layout.adapter_country_even);
        else
            return ViewInfo.setView(CountryView.class).withLayout(R.layout.adapter_country_odd);
    }
}

##### Example of different types of views based on item properties, using diferent view clases and layouts:

public class MultipleAdapter extends PresenterAdapter<Country> {

    @Override public ViewInfo getViewInfo(int position) {
        if((getItem(position).isFavourite())
            return ViewInfo.setView(FavouriteItemView.class).withLayout(R.layout.adapter_favourite_item);
        else
            return ViewInfo.setView(NormalItemView.class).withLayout(R.layout.adapter_normal_item);
    }

### Event listeners

Click and long click listeners methods are provided to be notified when users interacts with your views. Also, you can set a custom object listener to be manually invoked from your view class when you want. See sample for details. 


## Author

Víctor Manuel Pineda Murcia | http://vicpinm.github.io/PresenterAdapter/
