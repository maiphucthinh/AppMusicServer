package com.thin.music.server

import com.thin.music.model.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Service
import java.io.IOException


@Service
class SongServiceImpl : SongSevice {

    private fun getSongSearch(nameSong: String?): MutableList<ItemSearchOnline> {
        val listSong: MutableList<ItemSearchOnline> = ArrayList()
        try {
            val c: Document = Jsoup.connect(("https://chiasenhac.vn/tim-kiem?q="
                    + nameSong!!.replace(" ", "+")) +
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
                        listSong.add(ItemSearchOnline(null, linkImg, linkSong, title, singer))
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

        return listSong
    }

    override fun getLinkSong(linkSong: String?): Any? {
        try {
            val c = Jsoup.connect(linkSong).get()
            val els = c.select("div.tab-content").first().select("a.download_item")
            return if (els.size >= 2) {
                GetLinkMusic(els[1].attr("href"))
            } else {
                GetLinkMusic(els[0].attr("href"))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return null
    }

    override fun getAlbum(): Any {
        val chart: MutableList<ItemChartAlbum> = ArrayList()
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
                chart.add(ItemChartAlbum(linkSong, number, linkImage, nameSong, nameSinger, null, linkSong))
            }
        } catch (e: IOException) {
        }
        val songTheme = getSongTheme()
        val albums = getNewestAlbum()
        val results = mutableListOf<ItemMusicList<ItemChartAlbum>>()
        results.add(ItemMusicList("Top", chart))
        results.add(ItemMusicList("Newest Album", albums))
        results.add(ItemMusicList("Theme", songTheme))
        return results
    }


    private fun getPlaylistSearch(songName: String?): MutableList<ItemSearchOnline> {
        val listAlbums: MutableList<ItemSearchOnline> = ArrayList()

        val c =
                Jsoup.connect("https://chiasenhac.vn/tim-kiem?q="
                        + songName!!.replace(" ", "+") +
                        "&page_album=1&filter=").get()
        val els = c.select("div.tab-content").select("div.col")
        val childEls = els.select("div.card")
        for (child in childEls) {
            val linkImage = child.select("div.card-header").attr("style")
                    .replace("background-image: url(", "")
                    .replace(");", "")
            val linkAlbum = child.select("a").attr("href")
            val nameAlbum = child.select("a").attr("title")
            val nameArtist = child.select("p.card-text").text()
            listAlbums.add(ItemSearchOnline(null, linkImage, linkAlbum, nameAlbum, nameArtist))
        }
        return listAlbums
    }

    private fun getvideoSearch(link: String?): MutableList<ItemSearchOnline> {
        val listVideo: MutableList<ItemSearchOnline> = ArrayList()
        val c =
                Jsoup.connect("https://chiasenhac.vn/tim-kiem?q="
                        + link!!.replace(" ", "+") +
                        "&page_video=1&filter=").get()
        val els = c.select("div.tab-content").select("div.col")
        val childEls = els.select("div.card")
        for (child in childEls) {
            val linkImage = child.select("div.card-header").attr("style")
                    .replace("background-image: url(", "")
                    .replace(");", "")
            val linkAlbum = child.select("a").attr("href")
            val nameAlbum = child.select("a").attr("title")
            val nameArtist = child.select("p.card-text").text()
            listVideo.add(ItemSearchOnline(null, linkImage, linkAlbum, nameAlbum, nameArtist))
        }
        return listVideo
    }

    private fun getArtistSearch(artistName: String?): MutableList<ItemSearchOnline> {
        val listArtist: MutableList<ItemSearchOnline> = ArrayList()
        val c =
                Jsoup.connect("https://chiasenhac.vn/tim-kiem?q="
                        + artistName!!.replace(" ", "+") +
                        "&page_artist=1&filter=")
                        .get()
        val els = c.select("div.tab-content").select("a.search-line")
        for (childEls in els) {
            val linkArtist = childEls.select("a").attr("href")
            val nameArtist = childEls.select("a").text()
            var linkImg = childEls.select("img").attr("src")
            if (linkImg.equals("https://data.chiasenhac.com/imgs/no_cover.jpg")) {
                linkImg = null
            }
            listArtist.add(ItemSearchOnline(null, linkArtist, nameArtist, linkImg))
        }
        return listArtist
    }

    override fun getChildTheme(linkTheme: String): Any? {
        val themes: MutableList<ItemChartAlbum> = ArrayList()
        try {

            val c =
                    Jsoup.connect("https://chiasenhac.vn" + linkTheme).get()
            val els = c.select("div.d-table").select("div.card-footer")
            val childEls = els.select("div.card-footer")
            for (child in childEls) {
                val linkSong = child.select("div.name").select("a").attr("href")
                val nameSong = child.select("div.name").select("a").text()
                val linkSinger = child.select("div.author").select("a").attr("href")
                val artist = child.select("div.author").select("a").text()
                themes.add(ItemChartAlbum(nameSong, artist, linkSong, linkSinger))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return themes
    }

    override fun getAllSearch(songName: String?): Any? {
        val listAllSearchOnline = mutableListOf<ItemMusicList<ItemSearchOnline>>()
        val song = getSongSearch(songName)
        val playlist = getPlaylistSearch(songName)
        val videos = getvideoSearch(songName)
        val artist = getArtistSearch(songName)
        listAllSearchOnline.add(ItemMusicList("Bài hát", song))
        listAllSearchOnline.add(ItemMusicList("Playlist", playlist))
        listAllSearchOnline.add(ItemMusicList("MV", videos))
        listAllSearchOnline.add(ItemMusicList("Nghệ sĩ", artist))
        return listAllSearchOnline
    }

    override fun getAllArtistSong(linkArtist: String?): Any? {
        val lisArtistSong = mutableListOf<ItemMusicList<ItemChartAlbum>>()
        val songs = getArtistSong(linkArtist)
        lisArtistSong.add(ItemMusicList("Bài hát", songs))
        return lisArtistSong
    }

    private fun getArtistSong(linkArtist: String?): MutableList<ItemChartAlbum> {
        val songs = mutableListOf<ItemChartAlbum>()
        val c =
                Jsoup.connect("https://chiasenhac.vn" + linkArtist + "?tab=music")
                        .get()
        val els = c.select("div.tabs").select("li.media")
        for (childEls in els) {
            val number = childEls.select("span.counter").text()
            val linkImage = childEls.select("a").attr("href")
            val songName = childEls.select("a").attr("title")
            val nameArtist = childEls.select("div.author").select("a").text()
            val typeMusic = childEls.select("span.card-text").text()
            songs.add(ItemChartAlbum(null, number, linkImage, songName, nameArtist, typeMusic, null))
        }
        return songs
    }

    fun getSongTheme(): MutableList<ItemChartAlbum> {
        val songTheme: MutableList<ItemChartAlbum> = ArrayList()
        try {
            val c =
                    Jsoup.connect("https://chiasenhac.vn").get()
            val els = c.select("div.box_catalog").select("a")
            for (child in els) {
                val linkTheme = child.select("a").attr("href")
                val linkImage = child.select("a").attr("style")
                        .replace("background: url('", "").replace("') no-repeat;", "")
                songTheme.add(ItemChartAlbum(null, null, linkImage, null, null, null, linkTheme))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return songTheme
    }

    fun getNewestAlbum(): MutableList<ItemChartAlbum> {
        val newestAlbums: MutableList<ItemChartAlbum> = ArrayList()
        try {
            val doc: Document = Jsoup.connect("https://chiasenhac.vn/album-moi.html").get()
            val albumsNew = doc.select("div.content-wrap").select("div.col")
            for (child in albumsNew) {
                val linkAlbumsSong = "https://chiasenhac.vn" + child.select("h3.card-title")
                        .select("a").attr("href")
                val imgAlbumsSong = child.select("div.card-header").attr("style")
                        .replace("background-image: url(", "").replace(");", "")
                val nameAlbumsSong = child.select("h3.card-title").select("a").attr("title")
                val nameAlbumsSingle = child.select("p.card-text").select("a").text()
                val item = ItemChartAlbum()
                item.id = linkAlbumsSong
                item.songName = nameAlbumsSong
                item.artistName = nameAlbumsSingle
                item.linkImage = imgAlbumsSong
                item.linkSong = linkAlbumsSong

                newestAlbums.add(item)

            }


        } catch (e: IOException) {
        }

        return newestAlbums
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