package com.aurosaswatraj.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates


class FeedEntry{
    var Name:String=""
    var imageUrl:String=""
    var artist:String=""
    var releaseDate:String=""
    var summary:String=""
    override fun toString():String{
        return """
            Name:$Name
            Artist:$artist
            Release Date:$releaseDate
            Summary:$summary
            Image URL:$imageUrl
            """.trimIndent()
    }

}



class MainActivity : AppCompatActivity() {
    val Tag:String="MainActivity"

    var feedCachedURL="INVALIDIATED"
    val STATE_URL="feedURL"
    val STATE_LIMIT="feedLIMIT"


    private var downloadData:DownloadBackground?=null
    private var feedUrl:String="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"

    private var feedLimit:Int=10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //After Rotation
        if (savedInstanceState!=null)
        {
            feedUrl=savedInstanceState.getString(STATE_URL).toString()
            feedLimit=savedInstanceState.getInt(STATE_LIMIT)
        }

        Log.d(Tag,"Downloading Started!")
        downloadUrl(feedUrl.format(feedLimit))
        Log.d(Tag,"Downloading over")




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_items,menu)
        if (feedLimit==10)
        {
            menu?.findItem(R.id.mnuTop10)?.isChecked=true
        }
        else
        {
            menu?.findItem(R.id.mnuTop25)?.isChecked=false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.mnuFree ->
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.mnuPaid ->
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.mnuSong ->
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.mnuTop10 , R.id.mnuTop25 ->{
                if (!item.isChecked)
                {
                    item.isChecked=true
                    feedLimit=35-feedLimit
                    Log.d(Tag,"onOptionsItemSelected: ${item.title} setting feedlimit to $feedLimit")
                }
                else
                {
                    Log.d(Tag,"onOptionsItemSelected: ${item.title} setting feedlimit No chaneg")
                }
            }
            //To Refresh Options selected.>>!
            R.id.mnuRefresh -> feedCachedURL="INVALIDIATED"
            else ->
                return super.onOptionsItemSelected(item)
        }
        downloadUrl(feedUrl.format(feedLimit))
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL,feedUrl)
        outState.putInt(STATE_LIMIT,feedLimit)
    }




    private fun downloadUrl(feedUrl: String) {
        if(feedUrl!=feedCachedURL)
        {
            Log.d(Tag,"OnCreate is running")
            Log.d(Tag,"Download Starting AsyncTask")
            downloadData=DownloadBackground(this,xmlListView)
            downloadData?.execute(feedUrl)
            //Finally we need to update feed cached URL value
            Log.d(Tag,"Cached Feed updated")
            feedCachedURL=feedUrl
            Log.d(Tag,"OnCreate Finishes!")

        }
        else{
            Log.d(Tag,"downloadurl-Url not changed")
        }
            }

    override fun onDestroy()
    {
        super.onDestroy()
        downloadData?.cancel(true)
    }


    companion object{
        private class DownloadBackground(context:Context, listView:ListView): AsyncTask<String, Void, String>() {

            val Tag:String="dobackie"

            var propContext:Context by Delegates.notNull()
            var propListView:ListView by Delegates.notNull()

            init {
                propContext=context
                propListView=listView
            }




        override fun doInBackground(vararg Url: String?): String {
            Log.d(Tag,"DoinBackground Completed:${Url[0]}")
            //Step-1
            val rssfeed=downloadXML(Url[0])

            if (rssfeed.isEmpty())
            {

                Log.e(Tag,"do in background:Error in downloading")
            }

            return rssfeed
            //End of Step-1
        }

            private fun downloadXML(UrlPath: String?):String{

                return URL(UrlPath).readText()

            }


        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parseApplication=ParseApplication()
            parseApplication.parse(result)

//            var arrayAdapter=ArrayAdapter<FeedEntry>(propContext,R.layout.list_record,parseApplication.applications)
//            //To display data in the adapter.......
//            propListView.adapter=arrayAdapter
            //Custom Array Adapter...
            val feedAdapter=FeedAdapter(propContext,R.layout.list_record,parseApplication.applications)
            propListView.adapter=feedAdapter






        }




        }
    }




}