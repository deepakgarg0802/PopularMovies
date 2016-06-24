package com.example.deepakgarg.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Deepak Garg on 23-06-2016.
 */
public abstract class MyUtility {

    public static boolean addFavoriteItem(Context context, String favoriteItem){
        //Get previous favorite items
        String favoriteList = getStringFromPreferences(context,null,"favorites");
        // Append new Favorite item
        boolean marked=false;
        if(favoriteList!=null && favoriteList.equals("")==false)
        {
            favoriteList = favoriteList+","+favoriteItem;
        }else{
            favoriteList = favoriteItem;
        }
        Log.d("fav",favoriteList);

        // Save in Shared Preferences
        return putStringInPreferences(context,favoriteList,"favorites");
    }
    public static boolean removeFavoriteItem(Context context, String favoriteItem){
        //Get previous favorite items
        String favoriteList = getStringFromPreferences(context,null,"favorites");
        boolean marked=false;
        if(favoriteList!=null)
        {
            if(favoriteList.contains(favoriteItem) && favoriteList.indexOf(favoriteItem+",")==0)
                favoriteList=favoriteList.replace(favoriteItem+","  ,"");
            else if(favoriteItem.equals(favoriteList))
            {
                favoriteList="";
            }
            else
            {
                favoriteList=favoriteList.replace(","+favoriteItem  ,"");
            }
        }
        // Save in Shared Preferences
        Log.d("fav",favoriteList);
        return putStringInPreferences(context,favoriteList,"favorites");
    }
    public static boolean checkFav(Context context, String favoriteItem)
    {
        //Get previous favorite items
        String favoriteList = getStringFromPreferences(context,null,"favorites");
        if(favoriteList==null || favoriteList.equals(""))
        {
            return false;
        }
        return favoriteList.contains(favoriteItem);
    }

    public static String[] getFavoriteList( Context context){
        String favoriteList = getStringFromPreferences(context,null,"favorites");
        return convertStringToArray(favoriteList);
    }
    private static boolean putStringInPreferences(Context context,String nick,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_fav",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, nick);
        editor.commit();
        //editor.apply();
        return true;
    }
    private static String getStringFromPreferences(Context context,String defaultValue,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_fav",Context.MODE_PRIVATE);
        String temp = sharedPreferences.getString(key, defaultValue);
        return temp;
    }

    private static String[] convertStringToArray(String str){
        if(str==null)
        {
            return null;
        }
        String[] arr = str.split(",");
        return arr;
    }
}