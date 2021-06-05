package com.aurosaswatraj.top10downloader

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.net.URL


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(Tag,"OnCreate is running")

        val Dobackie=dobackie()

        Dobackie.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(Tag,"OnCreate Finishes!")

    }

    companion object{
        private class dobackie: AsyncTask<String, Void, String>() {

            val Tag:String="dobackie"




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

        }




        }
    }




}