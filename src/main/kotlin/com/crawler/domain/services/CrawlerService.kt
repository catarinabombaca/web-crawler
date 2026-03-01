package com.crawler.domain.services

import com.crawler.domain.ports.Crawler
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.jsoup.Jsoup

class CrawlerService(
    private val client: HttpHandler,
) : Crawler {
    override fun crawl(seedUrl: String): List<String> {
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<String>()
        queue.add(seedUrl)

        while (queue.isNotEmpty()) {
            val url = queue.removeFirst()
            if (url in visited) continue

            val html = client(Request(Method.GET, url)).bodyString()
            val urlsFound = extractUrls(html, url)

            visited.add(url)

            urlsFound
                .filter { it !in visited }
                .forEach { queue.add(it) }
        }

        return visited.toList()
    }

    private fun extractUrls(html: String, seedUrl: String): List<String> {
        val doc = Jsoup.parse(html, seedUrl)
        return doc.select("a[href]")
            .map { it.absUrl("href") }
            .filter { it.startsWith("http://") || it.startsWith("https://") }
    }
}