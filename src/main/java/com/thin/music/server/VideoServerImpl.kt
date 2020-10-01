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
    override fun getVideo(): Any? {
        val onlines: MutableList<ItemVideoOnline> = ArrayList()
        try {
            val doc = Jsoup.connect("https://chiasenhac.vn/video-moi.html").get()
            val els = doc.select("div.content-wrap").select("div.col")
                for (child in els) {
                    val linkImage = child.select("div.card-header").attr("style").replace("background-image: url(", "").replace(");", "")
                    val linkVideo = child.select("a").attr("href")
                    val nameVideo = child.select("a").attr("title")
                    val artistName = child.select("p").select("a").text()

                    onlines.add(ItemVideoOnline(null, linkImage, linkVideo, nameVideo, artistName))
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return onlines
    }

    override fun getLinkVideo(linkVideo: String): Any {
        val c =
                Jsoup.connect(linkVideo)
                        .get()
        val els = c.select("div.col-12").select("li")
        val childEls = els.select("li")
       return if (childEls.size >= 2) {
           GetLinkMusic(
                   childEls.get(1).select("a").attr("href")
           )
//            val linkVideo = childEls.get(1).select("a").attr("href")
        } else {
           GetLinkMusic(
                   childEls.get(0).select("a").attr("href")
           )
//            val linkVideo1 = childEls.get(0).select("a").attr("href")
        }
    }
}