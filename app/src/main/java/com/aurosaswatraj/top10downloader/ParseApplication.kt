package com.aurosaswatraj.top10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory


class ParseApplication {

    private val Tag:String="ParseApplication"

    val applications=ArrayList<FeedEntry>()

    fun parse(xmlData:String):Boolean{
        var status:Boolean=true
        var inEntry:Boolean=false
        var textValue:String=""

        try{

            val factory = XmlPullParserFactory.newInstance()
            //XmlPullParserFactory class is used to create implementations of XML Pull Parser defined in XMPULL V1 API.
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var evnttype=xpp.eventType
            var currentRecord=FeedEntry()
            while(evnttype!=XmlPullParser.END_DOCUMENT){
                val tagname=xpp.name?.lowercase()
                when(evnttype)
                {
                    XmlPullParser.START_TAG-> {Log.d(Tag,"Parse : Starting Tag for "+ tagname)
                        if (tagname=="entry")
                        {
                            inEntry=true
                        }
                    }
                XmlPullParser.TEXT->{textValue=xpp.text}

                XmlPullParser.END_TAG->{
                //    Log.d(Tag,"Parse : Ending Tag for "+tagname)
                    if (inEntry)
                    {
                        when(tagname)
                        {
                            "entry"->{applications.add(currentRecord)
                            inEntry=false
                            currentRecord= FeedEntry()
                            }
                            "name"->currentRecord.Name=textValue
                            "artist"->currentRecord.artist=textValue
                            "releasedate"->currentRecord.releaseDate=textValue
                            "summary"->currentRecord.summary=textValue
                            "image"->currentRecord.imageUrl=textValue

                        }
                    }
                }
                }
                //Nothing Else to do...
                evnttype=xpp.next()
            }

            //To display items in logcat...
//            for (i in applications)
//            {
//                Log.d(Tag,"******************")
//                Log.d(Tag,i.toString())
//            }

        }
        catch(e:Exception){
            e.printStackTrace()
            status=false
        }
        return status
    }
}