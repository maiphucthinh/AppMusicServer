package com.thin.music.server

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
}