package com.thin.music.server

import com.thin.music.model.GetLinkMusic
import com.thin.music.model.ItemMusicOnline
import com.thin.music.model.ItemVideoOnline
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service
import java.io.IOException


@Service
class VideoServerImpl : VideoServer {
    val listVideoNews: MutableList<ItemVideoOnline> = ArrayList()
    override fun getVideo(): Any? {
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html")
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html?page=2")
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html?page=3")
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html?page=4")
        return listVideoNews
    }
    private fun getVideoNewsByLink(link:String){
        try {
            val doc = Jsoup.connect(link).get()
            val els = doc.select("div.content-wrap").select("div.col")
            for (child in els) {
                val linkImage = child.select("div.card-header").attr("style").replace("background-image: url(", "").replace(");", "")
                val linkVideo = child.select("a").attr("href")
                val nameVideo = child.select("a").attr("title")
                val artistName = child.select("p").select("a").text()

                listVideoNews.add(ItemVideoOnline(null, linkImage, linkVideo, nameVideo, artistName))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getLinkVideo(linkVideo: String): Any {
        val c =
                Jsoup.connect(linkVideo)
                        .get()
        val els = c.select("div.col-12").select("li")
        val childEls = els.select("li")
        return if (childEls.size >= 2) {
            GetLinkMusic(childEls.get(1).select("a").attr("href")
                    .replace(".html","thinh")   )
        } else {
            GetLinkMusic(childEls.get(0).select("a").attr("href")
                    .replace(".html","thinh") )
        }
    }
}