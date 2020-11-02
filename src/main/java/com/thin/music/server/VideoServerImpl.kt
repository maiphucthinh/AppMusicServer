package com.thin.music.server

import com.thin.music.model.GetLinkMusic
import com.thin.music.model.ItemMusicOnline
import com.thin.music.model.ItemSearchOnline
import com.thin.music.model.ItemVideoOnline
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service
import java.io.IOException


@Service
class VideoServerImpl : VideoServer {
    val listVideoNews: MutableList<ItemSearchOnline> = ArrayList()
    override fun getVideo(): Any? {
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html")
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html?page=2")
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html?page=3")
        getVideoNewsByLink("https://chiasenhac.vn/video-moi.html?page=4")
        return listVideoNews
    }

    private fun getVideoNewsByLink(link: String) {
        try {
            val doc = Jsoup.connect(link).get()
            val els = doc.select("div.content-wrap").select("div.col")
            for (child in els) {
                val linkImage = child.select("div.card-header").attr("style").replace("background-image: url(", "").replace(");", "")
                val linkVideo = child.select("a").attr("href")
                val nameVideo = child.select("a").attr("title")
                val artistName = child.select("p").select("a").text()

                listVideoNews.add(ItemSearchOnline(null, linkImage, linkVideo, nameVideo, artistName))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getLinkVideo(linkVideo: String): Any? {
        if (linkVideo == null){
            return null
        }
        val c =
                Jsoup.connect(linkVideo)
                        .get()
        val els = c.select("div.col-12").select("li")
        val els1 = c.select("div.card.card-details").select("ul.list-unstyled").select("li")
        val linkArtist = els1[0].select("a").attr("href")
        val artistName = els1[0].select("a").text()
        val authorName = els1[1].select("a").text()
        val linkAuthor = els1[1].select("a").attr("href")
        val lyr = c.select("div.tab-content.tab-lyric").text()
        val lyrics = lyricFix(lyr)
        val songName = c.select("div.card.card-details").select("h2.card-title").text()
        val childEls = els.select("li")
        return if (childEls.size >= 2) {
            GetLinkMusic(childEls.get(1).select("a").attr("href"),
                    lyrics,songName, artistName, linkArtist, authorName, linkAuthor)
        } else {
            GetLinkMusic(childEls.get(0).select("a").attr("href"),
                    lyrics,songName, artistName, linkArtist, authorName, linkAuthor)
        }
    }
    private fun lyricFix(lyrics: String):String {
        var index = 0
        var con = 0
        var lyr = ""
        for (i in 0..lyrics.length - 1) {
            val a = lyrics.get(i)
            if (a == '(' || a == '[') {
                con = i
            }
            if (a == ')' || a == ']') {
                lyr += lyrics.substring(con, i + 1)
                con = 0
                index = i + 1
            }
            if (a >= 'A' && a <= 'Z' && i > 2 && con == 0 || a == '[' || a == '(') {
                lyr = lyr + lyrics.substring(index, i - 1) + "\n"
                index = i
            }
        }
        return lyr
    }
}