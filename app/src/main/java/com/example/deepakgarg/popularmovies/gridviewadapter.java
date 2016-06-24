package com.example.deepakgarg.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Deepak Garg on 06-05-2016.
 */
public class gridviewadapter extends BaseAdapter {
    Context myContext;
    String name[];
    //ViewHolder viewHolder;
    String image_url[];
    LayoutInflater inflater;

    public gridviewadapter(Context context, String name[], String[] image_url) {
        this.myContext = context;
        this.name = name;
        this.image_url = image_url;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    public void updateAdapter(Context context, String name[], String[] image_url) {
        this.myContext = context;
        this.name = name;
        this.image_url = image_url;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       /* View rowView;
        //ViewHolder viewHolder;
        inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            rowView = inflater.inflate(R.layout.gridadapter, parent, false);
            //rowView=LayoutInflater.from(myContext).inflate(R.layout.gridadapter,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) rowView.findViewById(R.id.moviename);
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.imageView);
            rowView.setTag(viewHolder);
            Log.d("abc","convert");

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(viewHolder.imageView==null)
            Log.d("abc","img view null");

        if (image_url== null || name== null)//somearray.get(position) != null)
        {
            viewHolder.imageView.setImageResource(R.drawable.download);
            viewHolder.textView.setText("Refresh to see");
            Log.d("abc","img url null");
        }
        else{
            Picasso.with(myContext)
                    .load("http://image.tmdb.org/t/p/w185/" + image_url[position])
                    .error(R.drawable.download)
                    .into(viewHolder.imageView);
            viewHolder.textView.setText(name[position]);
        }
        return convertView;
    }

    class ViewHolder {
        // declare your views here
        TextView textView;
        ImageView imageView;
    }


}
*/
        View grid;
        LayoutInflater inflater= (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid = new View(myContext);
        grid = inflater.inflate(R.layout.gridadapter, null);
        TextView textView = (TextView) grid.findViewById(R.id.moviename);
        ImageView imageView = (ImageView) grid.findViewById(R.id.imageView);
        if (image_url != null && name != null) {
            Picasso.with(myContext).load("http://image.tmdb.org/t/p/w185/" + image_url[position])
                    .error(R.drawable.download)
                    .into(imageView);
            textView.setText(name[position]);

        } else {
            imageView.setImageResource(R.drawable.download);
            textView.setText("Refresh to see");
        }
        return grid;
    }
}

