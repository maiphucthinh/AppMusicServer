package com.thin.music.server

import com.thin.music.model.ItemChart
import com.thin.music.model.ItemMusicOnline
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Service
import java.io.IOException


@Service
class SongServiceImpl : SongSevice {
    override fun searchSong(name: String?): Any {
        if (name == null) {
            return getListFirstSong()
        }
        var songName = name
        val onlines: MutableList<ItemMusicOnline> = java.util.ArrayList()
        try {
            val c: Document = Jsoup.connect(("https://chiasenhac.vn/tim-kiem?q="
                    + songName.replace(" ", "+")) +
                    "&page_music=" + "1" + "&filter=all").get()
            val els: Elements = c.select("div.tab-content").first().select("ul.list-unstyled")
            for (i in 0..els.size - 1) {
                val e: Element = els.get(i)
                val childEls: Elements = e.select("li.media")
                for (child in childEls) {
                    try {
                        val linkSong: String = child.select("a").first().attr("href")
                        val linkImg: String = child.select("a").first().select("img").attr("src")
                        val title: String = child.select("a").first().attr("title")
                        val singer: String = child.select("div.author").text()
                        onlines.add(ItemMusicOnline(linkSong, linkImg, title, singer, linkSong))
                    } catch (e1: Exception) {
                        e1.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return onlines
    }

    override fun getLinkSong(linkSong: String?): Any? {
        TODO("Not yet implemented")
    }

    override fun getChart(): Any {
        val chart: MutableList<ItemChart> = ArrayList()
        try {
            val doc = Jsoup.connect("https://chiasenhac.vn/bang-xep-hang/tuan.html").get()
            val els = doc.select("div.tab-content").select("ul.list-unstyled")
            val childEls = els.select("li.media")
            for (child in childEls) {
                val number = child.select("span.counter").text()
                val linkSong = "https://chiasenhac.vn" + child.select("a").first().attr("href")
                val nameSong = child.select("a").first().attr("title")
                val linkImage = child.select("a").select("img").attr("src")
                val nameSinger = child.select("div.author").text()
                chart.add(ItemChart(linkSong, number, linkImage, nameSong, nameSinger, linkSong))
            }
        } catch (e: IOException) {
        }
        return chart

    }

    private fun getListFirstSong(): Any {
        val onlines: MutableList<ItemMusicOnline> = java.util.ArrayList()
        try {
            val doc = Jsoup.connect("https://chiasenhac.vn/bang-xep-hang/tuan.html").get()
            val els = doc.select("div.tab-content").select("ul.list-unstyled")
            val childEls = els.select("li.media")
            for (child in childEls) {
                val linkSong = "https://chiasenhac.vn" + child.select("a").first().attr("href")
                val nameSong = child.select("a").first().attr("title")
                val linkImage = child.select("a").select("img").attr("src")
                val nameSinger = child.select("div.author").text()
                onlines.add(ItemMusicOnline(linkSong, linkImage, nameSong, nameSinger, linkSong))
            }
        } catch (e: IOException) {
        }
        return onlines
    }
}