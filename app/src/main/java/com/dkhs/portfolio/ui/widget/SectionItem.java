/**
 * @Title SectionItem.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-23 下午2:58:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

/**
 * @ClassName SectionItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-23 下午2:58:51
 * @version 1.0
 */
public class SectionItem<T> {
    
    
    // ListView list = new ListView(this);
    //
    // SimpleSectionsAdapter<String> adapter = new SimpleSectionsAdapter<String>(
    // list, /* Context for resource inflation */
    // R.layout.list_header, /* Layout for header views */
    // android.R.layout.simple_list_item_1 /* Layout for item views */
    // ) {
    // //Click handler for item taps
    // @Override
    // public void onSectionItemClick(String item) {
    // Toast.makeText(SectionsActivity.this, item, Toast.LENGTH_SHORT).show();
    // }
    // };
    //
    // adapter.addSection("Fruits", new String[]{"Apples", "Oranges", "Bananas", "Mangos"});
    // adapter.addSection("Vegetables", new String[]{"Carrots", "Peas", "Broccoli"});
    // adapter.addSection("Meats", new String[]{"Pork", "Chicken", "Beef", "Lamb"});
    //
    // list.setAdapter(adapter);
    // 
    
    private String mTitle;
    private T[] mItems;

    public SectionItem(String title, T[] items) {
        if (title == null)
            title = "";

        mTitle = title;
        mItems = items;
    }

    public String getTitle() {
        return mTitle;
    }

    public T getItem(int position) {
        return mItems[position];
    }

    public int getCount() {
        // Include an additional item for the section header
        return (mItems == null ? 1 : 1 + mItems.length);
    }

    @Override
    public boolean equals(Object object) {
        // Two sections are equal if they have the same title
        if (object != null && object instanceof SectionItem) {
            return ((SectionItem) object).getTitle().equals(mTitle);
        }
        return false;
    }
}
