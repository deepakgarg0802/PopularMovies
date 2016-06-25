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

import java.util.ArrayList;

/**
 * Created by Deepak Garg on 06-05-2016.
 */
public class GridViewAdapter extends BaseAdapter {
    Context myContext;
    ArrayList<Thumnail>thumnails=null;
    LayoutInflater inflater;

    public GridViewAdapter(Context context, ArrayList<Thumnail>thumnails) {
        this.myContext = context;
        this.thumnails=thumnails;
    }

    public ArrayList<Thumnail> getArrayListItems()
    {
        return this.thumnails;
    }
    public void setArrayListItems(ArrayList<Thumnail> arrayListItems)
    {
        this.thumnails=arrayListItems;
    }

    @Override
    public int getCount() {
        return thumnails.size();
    }

    public void updateAdapter(Context context, ArrayList<Thumnail>thumnails) {
        this.myContext = context;
        this.thumnails=thumnails;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        //return null;
        return thumnails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

/*        View rowView;
        ViewHolder viewHolder;
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

        if (thumnails.get(position)==null)//somearray.get(position) == null)
        {
            viewHolder.imageView.setImageResource(R.drawable.download);
            viewHolder.textView.setText("Refresh to see");
            Log.d("abc","img url null");
        }
        else{
            Picasso.with(myContext)
                    .load("http://image.tmdb.org/t/p/w185/" + thumnails.get(position).getImage_url())
                    .error(R.drawable.download)
                    .into(viewHolder.imageView);
            viewHolder.textView.setText(thumnails.get(position).getName());
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
        if (thumnails.get(position)==null || thumnails.get(position).getImage_url()==null) {
            imageView.setImageResource(R.drawable.download);
            textView.setText("Refresh to see");

        } else {
            Picasso.with(myContext).load("http://image.tmdb.org/t/p/w185/" + thumnails.get(position).getImage_url())
                    .error(R.drawable.download)
                    .into(imageView);
            textView.setText(thumnails.get(position).getName());
        }
        return grid;
    }
}

