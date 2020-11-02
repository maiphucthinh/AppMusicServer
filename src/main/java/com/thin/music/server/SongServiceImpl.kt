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

    private fun getSongSearch(nameSong: String?): MutableList<ItemChartAlbum> {
        val listSong: MutableList<ItemChartAlbum> = ArrayList()
        if (nameSong == null) {
            return listSong
        }
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
                        listSong.add(ItemChartAlbum(
                                null, null, linkImg, title, singer, null, linkSong, null))

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
            val els1 = c.select("div.card.card-details").select("ul.list-unstyled").select("li")
            val linkArtist = els1[0].select("a").attr("href")
            val artistName = els1[0].select("a").text()
            val authorName = els1[1].select("a").text()
            val linkAuthor = els1[1].select("a").attr("href")
            val els = c.select("div.tab-content").first().select("a.download_item")
            val lyr = c.select("div.tab-content.tab-lyric").text()
            var lyrics = lyricFix(lyr)
            val songName = c.select("div.card.card-details").select("h2.card-title").text()
            return if (els.size >= 2) {
                GetLinkMusic(els[1].attr("href"),
                        lyrics, songName, artistName, linkArtist, authorName, linkAuthor)
            } else {
                GetLinkMusic(els[0].attr("href"),
                        lyrics, songName, artistName, linkArtist, authorName, linkAuthor)
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
                chart.add(ItemChartAlbum(
                        linkSong, number, linkImage, nameSong, nameSinger, null, linkSong, null))
            }
        } catch (e: IOException) {
        }
        val listTheme = getListTheme()
        val songTheme = getSongTheme()
        val albums = getNewestAlbum()
        val results = mutableListOf<ItemMusicList<ItemChartAlbum>>()
        val downLoaded = getJustDownloaded()
        val favoriteSinger = getFavoriteSinger()
        val newAlbum = getNewAlbum()
        results.add(ItemMusicList("Chủ đề", listTheme))
        results.add(ItemMusicList("Top", chart))
        results.add(ItemMusicList("Newest Album", albums))
        results.add(ItemMusicList("Theme", songTheme))
        results.add(ItemMusicList("Vừa Download", downLoaded))
        results.add(ItemMusicList("Ca sĩ yêu thích", favoriteSinger))
        results.add(ItemMusicList("Album mới", newAlbum))
        return results
    }


    private fun getPlaylistSearch(songName: String?): MutableList<ItemChartAlbum> {
        val listAlbums: MutableList<ItemChartAlbum> = ArrayList()

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
            listAlbums.add(ItemChartAlbum(null, linkImage, nameArtist, linkAlbum, nameAlbum))
        }
        return listAlbums
    }

    private fun getvideoSearch(link: String?): MutableList<ItemChartAlbum> {
        val listVideo: MutableList<ItemChartAlbum> = ArrayList()
        if (link == null) {
            return listVideo
        }
        val c =
                Jsoup.connect("https://chiasenhac.vn/tim-kiem?q="
                        + link!!.replace(" ", "+") + "&page_video=1&filter=")
                        .get()
        val els = c.select("div.tab-content").select("div.card.card1.video")
        for (child in els) {
            val linkImage = child.select("div.card-header").attr("style")
                    .replace("background-image: url(", "").replace(");", "")
            val linkMusic = child.select("a").attr("href")
            val title = child.select("a").attr("title")
            val artistName = child.select("p").text()
            listVideo.add(ItemChartAlbum(
                    null, null, linkImage, title, artistName, null, linkMusic, null))
        }
        return listVideo
    }

    private fun getArtistSearch(artistName: String?): MutableList<ItemChartAlbum> {
        val listArtist: MutableList<ItemChartAlbum> = ArrayList()
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
            listArtist.add(ItemChartAlbum(
                    null, null, linkImg, null, nameArtist, null, null, linkArtist))
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
        val listAllSearchOnline = mutableListOf<ItemMusicList<ItemChartAlbum>>()
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
        if (linkArtist == null || linkArtist.equals("")) {
            return null
        }
        try {
            val c =
                    Jsoup.connect("https://chiasenhac.vn" + linkArtist).get()
            val linkImgae = c.select("div.box_profile").select("img").attr("src")
            val lisArtistSong = mutableListOf<ItemMusicList<ItemChartAlbum>>()
            val songs = getArtistSong(linkArtist)
            lisArtistSong.add(ItemMusicList(linkImgae, songs))
            return lisArtistSong
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    override fun getLinkTheme(linkTheme: String?): Any? {
        val listTheme: MutableList<ItemChartAlbum> = ArrayList()
        try {
            val c =
                    Jsoup.connect("https://chiasenhac.vn" + linkTheme).get()
            val els = c.select("div.row.row10px.float-col-width").select("div.col")
            for (child in els) {
                val linkAlbum = child.select("a").attr("href")
                val linkImage = child.select("div.card-header").attr("style")
                        .replace("background-image: url(", "").replace(");", "")
                val title = child.select("a").attr("title")
                val artistName = child.select("p.card-text").select("a").text()
                listTheme.add(ItemChartAlbum(null, linkImage, artistName, linkAlbum, title))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listTheme
    }

    private fun getArtistSong(linkArtist: String?): MutableList<ItemChartAlbum> {
        val songs = mutableListOf<ItemChartAlbum>()
        val c =
                Jsoup.connect("https://chiasenhac.vn" + linkArtist + "?tab=music")
                        .get()
        val els = c.select("div.tabs").select("li.media")
        for (childEls in els) {
            val number = childEls.select("span.counter").text()
            val linkImage = childEls.select("img").attr("src")
            val linkSong = childEls.select("a").attr("href")
            val songName = childEls.select("a").attr("title")
            val nameArtist = childEls.select("div.author").select("a").text()
            val typeMusic = childEls.select("span.card-text").text()
            songs.add(ItemChartAlbum(
                    null, number, linkImage, songName, nameArtist, typeMusic, linkSong, null))
        }
        return songs
    }

    private fun getFavoriteSinger(): MutableList<ItemChartAlbum> {
        val listArtist = mutableListOf<ItemChartAlbum>()
        try {
            val c =
                    Jsoup.connect("https://chiasenhac.vn/#")
                            .get()
            val els = c.select("div.singer_grid").select("a")
            for (child in els) {
                var linkImage = child.select("a").attr("style")
                        .replace("background-image: url(", "").replace(");", "")
                val linkMusic = child.select("a").attr("href")
                val artist = child.select("a").attr("title")
                listArtist.add(ItemChartAlbum(
                        null, null, linkImage, artist, artist, null, linkMusic, null))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listArtist
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
                val title = child.select("span").text()
                songTheme.add(ItemChartAlbum(
                        null, null, linkImage, title, title, null, linkTheme, null))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return songTheme
    }

    fun getListTheme(): MutableList<ItemChartAlbum> {
        val listTheme: MutableList<ItemChartAlbum> = ArrayList()
        try {
            val c =
                    Jsoup.connect("https://chiasenhac.vn/vua-download.html?tab=vua-download")
                            .get()
            val els = c.select("div.col-md-3").select("a")
            for (child in els) {
                var linkImage = child.select("a").attr("style")
                        .replace("background-image: url('", "").replace("')", "")
                linkImage = "https://chiasenhac.vn" + linkImage
                val linkMusic = child.select("a").attr("href")
                val title = child.select("a").attr("title")
                listTheme.add(
                        ItemChartAlbum(
                                null, null, linkImage, title,
                                null, null, linkMusic, null))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listTheme
    }

    private fun getJustDownloaded(): MutableList<ItemChartAlbum> {
        val justDownloaded: MutableList<ItemChartAlbum> = ArrayList()
        val c =
                Jsoup.connect("https://chiasenhac.vn/vua-download.html?tab=vua-download")
                        .get()
        val els = c.select("div.content-wrap.tab-content-category").select("li.media.align-items-stretch.not")
        for (child in els) {
            val linkImage = child.select("img").attr("src")
            val linkMusic = child.select("a").attr("href")
            val title = child.select("a").attr("title")
            val artistName = child.select("div.author").select("a").text()
            justDownloaded.add(
                    ItemChartAlbum(
                            null, null, linkImage, title, artistName, null, linkMusic, null))
        }
        return justDownloaded
    }

    private fun getNewAlbum(): MutableList<ItemChartAlbum> {
        val listAlbum: MutableList<ItemChartAlbum> = ArrayList()
        val c =
                Jsoup.connect("https://chiasenhac.vn/album-moi.html").get()
        val els = c.select("div.content-wrap.tab-content-category").select("div.col")
        for (child in els) {
            val linkAlbum = child.select("a").attr("href")
            val linkImage = child.select("div.card-header").attr("style")
                    .replace("background-image: url(", "").replace(");", "")
            val title = child.select("a").attr("title")
            val artistName = child.select("p.card-text").select("a").text()
            val item = ItemChartAlbum()
            item.id = linkAlbum
            item.songName = title
            item.artistName = artistName
            item.linkImage = linkImage
            item.linkSong = linkAlbum

            listAlbum.add(item)

        }
        return listAlbum
    }

    fun getNewestAlbum(): MutableList<ItemChartAlbum> {
        val newestAlbums: MutableList<ItemChartAlbum> = ArrayList()
        try {
            val c =
                    Jsoup.connect("https://chiasenhac.vn").get()
            val els = c.select("div.row.row10px.float-col-width").select("div.col")
            for (child in els) {
                val linkAlbum = child.select("a").attr("href")
                val linkImage = child.select("div.card-header").attr("style")
                        .replace("background-image: url(", "").replace(");", "")
                val title = child.select("a").attr("title")
                val artistName = child.select("p.card-text").select("a").text()

                newestAlbums.add(
                        ItemChartAlbum(
                                null, null, linkImage, title, artistName,
                                null, linkAlbum, null))

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
                val linkSong = "https://chiasenhac.vn" + child.select("a")
                        .first().attr("href")
                val nameSong = child.select("a").first().attr("title")
                val linkImage = child.select("a").select("img").attr("src")
                val nameSinger = child.select("div.author").text()
                onlines.add(ItemMusicOnline(linkSong, linkImage, nameSong, nameSinger, linkSong))
            }
        } catch (e: IOException) {
        }
        return onlines
    }


    private fun lyricFix(lyrics: String): String? {
        if (lyrics == null) {
            return null
        }
        try {
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
                    if (i == lyrics.length - 1) {
                        index = i
                    }
                    index = i + 1
                }
                if (a >= 'A' && a <= 'Z' && i > 0 && con == 0 || a == '[' || a == '(') {
                    if (index != i) {
                        lyr = lyr + lyrics.substring(index, i - 1) + "\n"
                        index = i
                    }
                }
            }
            return lyr
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}