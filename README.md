# PresenterAdapter

Lighweight Android library to help you implement MVP pattern for your RecyclerView adapters in a clean way.

## Features

  * Avoid implementing adapter clases, you only has to focus in views clases and their presenters
  * Separates view representation with view logic with MVP pattern
  * Easy creation of different kinds of views for the same RecyclerView.
  * Avoid create new presenter for each row in the list, presenter instances are recycled in the same way that adapter does with ViewHolder clases.
  * Lifecycle callbacks in presenter clases. You can control view creation and destroy for each RecyclerView position.
  * Custom presenter creation. You are responsible for creating presenter instance the same way yo usually do in your Activities or Fragments, which allows you to use tools like Dagger to inject your dependencies.

## Usage
### Single view type adapter

For adapters with a unique kind of view, there is no need to create any adapter class. SimplePresenterAdapter is provided for this kind of lists.
 
##### Adapter creation sample for a list of countries. 
  CountryView.java is the class which implements the view layer for each adapter position, the same way Activities or Fragment does.
  Data setter is optional and can be setted in any moment later.
             
        PresenterAdapter<Country> adapter = SimplePresenterAdapter.with(CountryView.class)
        .setLayout(R.layout.adapter_country)
        .setData(data);

### View class

Class responsible for implementing the view layer in MPV pattern, equivalent to Activities or Fragments. It inherits from ViewHolder<Data> class. 
This class must implement two methods, createPresenter() and getPresenter(). You control presenter creation to allow using tools like Dagger.
See CountryView.java class in sample module for details.

### Presenter class

Class responsible for implementing the presenter layer in MPV pattern, equivalent to any other presenter. It inherits from ViewHolderPresenter<Data, PresenterView> class. 
This class is generic and you need to indicate to types, your adapter data type and your presenter view interface. 
You have to override onCreate method to iniciate your view. Also, you can override onDestroy method if you need to implement any destroy logic for your view.
Inside your presenter class, you have access to getData() method, in order to get the current data instance for the view, obtained from the adapter data.
Also, inside your adapter class, you have accdess to getView() method, in order to interact with your view class.
See CountryPresenter.java class in sample module for details.

### Multiple view type adapter

It is very easy to implement multiple view types in your adapter. In this case, instead of use SimpleAdapterPresenter, you have to implement your own adapter, in order to implement your representation logic.
PresenterAdapter parent class has only one abstract method you have to implement, getViewInfo(int position) method. This method return an intance of ViewInfo class, which holds an association between your view class and your layour resource.
This association could be in the ViewHolder class itself, with a method in this class that return the layout associated this each view class. But this approximation is less flexible because you can't specify diferent layouts for the same view class.

Example of different types of views based on item position, using the same view class and differents layouts:

    public class MultipleAdapter extends PresenterAdapter<Country> {

    @Override public ViewInfo getViewInfo(int position) {
        if(position % 2 == 0)
            return ViewInfo.createView(CountryView.class).withLayout(R.layout.adapter_country_even);
        else
            return ViewInfo.createView(CountryView.class).withLayout(R.layout.adapter_country_even);
    }
}

Example of different types of views based on item property, using diferent view clases and layout:

public class MultipleAdapter extends PresenterAdapter<Country> {

    @Override public ViewInfo getViewInfo(int position) {
        if((getItem(position).isImportant())
            return ViewInfo.createView(ImportantItemView.class).withLayout(R.layout.adapter_important_item);
        else
            return ViewInfo.createView(NormalItemView.class).withLayout(R.layout.adapter_normal_item);
    }



## Author

Víctor Manuel Pineda Murcia | http://vicpinm.github.io/PresenterAdapter/
