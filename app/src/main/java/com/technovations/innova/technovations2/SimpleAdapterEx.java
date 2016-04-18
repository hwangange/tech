package com.technovations.innova.technovations2;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class SimpleAdapterEx extends SimpleAdapter {
    private int mResource;
    private int[] mTo;
    private String[] mFrom;

    private List<? extends Map<String, ?>> mData;

    public SimpleAdapterEx(Context context, List<? extends Map<String, ?>> data,
                           int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        mResource = resource;
        mData = data;
        mTo = to;
        mFrom = from;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        v = createViewFromResourceEx(v, position, convertView, parent, mResource);

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getDropDownView(position, convertView, parent);

        v = createViewFromResourceEx(v, position, convertView, parent, mResource);

        return v;
    }

    private View createViewFromResourceEx(View v, int position, View convertView,
                                          ViewGroup parent, int resource) {

        bindView(position, v);

        return v;
    }

    private void bindView(int position, View view) {
        final Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        final ViewBinder binder = getViewBinder();
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }

                if (!bound) {
                    if (v instanceof ImageView) {
                        if (data instanceof String) {
                            setViewImage((ImageView) v, (String)data);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setViewImage(ImageView imageView, String data) {
        imageView.setImageBitmap(convert(data));
    }

    private Bitmap convert (String str) {
        //byte[] decodedString = Base64.decode(str, Base64.DEFAULT);

        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
        byte[] arr = Base64.decode(str, flags);

        Bitmap bmp = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        return bmp;


        /*byte[] decodedString = str.getBytes();
        String base64 = Base64.encodeToString(decodedString, Base64.URL_SAFE);
        decodedString = Base64.decode(base64, Base64.URL_SAFE);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap; */
    }
}

