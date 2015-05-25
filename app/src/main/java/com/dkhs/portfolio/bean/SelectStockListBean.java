package com.dkhs.portfolio.bean;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/5/25.
 */
public class SelectStockListBean implements ParcelConverter<List<SelectStockBean>>

{
    @Override
    public void toParcel(List<SelectStockBean> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        } else {
            parcel.writeInt(input.size());
            for (SelectStockBean item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public List<SelectStockBean> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<SelectStockBean> items = new ArrayList<SelectStockBean>();
        for (int i = 0; i < size; ++i) {
            items.add((SelectStockBean) Parcels.unwrap(parcel.readParcelable(SelectStockBean.class.getClassLoader())));
        }
        return items;
    }
}
