package com.aurosaswatraj.top10downloader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Declare a viewHolder class to use the ViewHolder Pattern!!

class ViewHolder(view:View){
    val tvName:TextView=view.findViewById(R.id.tvName)
    val tvArtist:TextView=view.findViewById(R.id.tvArtist)
    val tvSummary:TextView=view.findViewById(R.id.tvSummary)
}

class FeedAdapter(context:Context,private val resource:Int,private val applications:List<FeedEntry>):ArrayAdapter<FeedEntry>(context, resource) {

    //Basically it contains two compulsory methods getCount and getView.......and some other to customize adapter

//    private val TAG:String="FeedAdapter"
    private var inflator=LayoutInflater.from(context)

    //To get number of items in the list.
    override fun getCount(): Int {
//        Log.d(TAG,"getCount() is called!!")
        return applications.size
    }



    //To create every new view.........after new view is recycled!!
    //Get a View that displays the data at the specified position in the data set.

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

//        Log.d(TAG,"getView() is called!! $position")

        //For more efficient Scroll

        val view:View
        val viewHolder:ViewHolder


        if(convertView==null)
        {
//            Log.d(TAG,"getView called with null ConvertView")
            view=inflator.inflate(resource,parent,false)
            //Initialize ViewHolder to view.tag attribute
            viewHolder=ViewHolder(view)
            view.tag=viewHolder
        }else{
//            Log.d(TAG,"getView provided with a convertView")
            view=convertView
            //set viewHolder with view.tag as ViewHolder class
            viewHolder=view.tag as ViewHolder
        }




//
//        val tvName:TextView=view.findViewById(R.id.tvName)
//        val tvArtist:TextView=view.findViewById(R.id.tvArtist)
//        val tvSummary:TextView=view.findViewById(R.id.tvSummary)

        viewHolder.tvName.text=applications[position].Name
        viewHolder.tvArtist.text=applications[position].artist
        viewHolder.tvSummary.text=applications[position].summary

        return view
    }
}