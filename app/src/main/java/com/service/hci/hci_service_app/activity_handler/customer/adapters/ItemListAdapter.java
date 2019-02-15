package com.service.hci.hci_service_app.activity_handler.customer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.data_types.Item;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ItemListAdapter extends ArrayAdapter<Item> {

    private Context context;
    private int ressource;
    private int lastPosition = -1;

    public ItemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Item> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Item item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String description = getItem(position).getDescription();
        int picture = getItem(position).getPicture();

        // create item object with information
        Item item = new Item(description, name, picture);

        // create the view result for showing the aniomation
        final View result;

        // viewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(ressource, parent, false);

            // to show animation
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.textView_customer_name_item);
            holder.description = (TextView) convertView.findViewById(R.id.textView_customer_description_item);
            holder.picture = (ImageView) convertView.findViewById(R.id.imgView_customer_picture_item);

            result = convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


//        Animation animation = AnimationUtils.loadAnimation(
//                context, (position > lastPosition) ? R.anim.anim_down_loader : R.anim.anim_up_loader);
//
//        result.startAnimation(animation);
//        lastPosition = position;

        SpannableString itemDescription = new SpannableString(item.getDescription());
        itemDescription.setSpan(new StyleSpan(Typeface.ITALIC), 0, itemDescription.length(), 0);
        holder.name.setText(item.getName());
        holder.description.setText(itemDescription);
        holder.picture.setImageResource(item.getPicture());

        return convertView;
    }

    private BitmapDrawable getResizedImageBitmap(View convertView, int picture) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(convertView.getResources(),
                picture);

        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        int newWidth = 60;
        int newHeight = 60;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // rotate the Bitmap
        matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever
        return new BitmapDrawable(convertView.getResources(), resizedBitmap);


    }

    static class ViewHolder {
        TextView name;
        TextView description;
        ImageView picture;
    }
}
