package ua.motofun.rss.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ua.motofun.rss.R;
import ua.motofun.rss.utils.TypefaceUtils;

/**
 * Created by sergeyelizarob on 23.02.16.
 */
public class Phone implements Serializable {

    public static ArrayAdapter getAdapter(Context context) {
        return  new Adapter(context, getPhones());
    }

    public static List<Phone> getPhones() {
        List<Phone> result = new ArrayList<>();
        result.add(new Phone("+380976400200", "Влад","Продажи, консультация по ремонту"));
        result.add(new Phone("+380739770870", "Сергей", "Продажи, консультация по прошивке"));
        result.add(new Phone("+380667636476", "Владислав", "Продажи"));
        result.add(new Phone("89587568696", "Сергей", "Прошивка, разработка ПО"));
        return result;
    }

    private String number;
    private String title;
    private String description;

    public Phone(String number, String title, String description) {
        this.number = number;
        this.title = title;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public static class Adapter extends ArrayAdapter<Phone> {

        public Adapter(Context context, List<Phone> objects) {
            super(context, -1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getContext(), R.layout.item_phone, null);
            Phone phone = getItem(position);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            title.setTypeface(TypefaceUtils.getRobotoRegular(getContext()));
            subtitle.setTypeface(TypefaceUtils.getRobotoLight(getContext()));
            title.setText(phone.getNumber());
//            subtitle.setText(phone.getDescription());
            return convertView;
        }
    }
}
