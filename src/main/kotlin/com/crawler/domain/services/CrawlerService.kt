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
        val domain = "https://${seedUrl.split("/")[2]}"
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<String>()

        queue.add(seedUrl)

        while (queue.isNotEmpty()) {
            val url = queue.removeFirst()
            if (url in visited) continue

            val html = client(Request(Method.GET, url)).bodyString()
            val urlsFound = extractUrls(html, url)

            visited.add(url)

            val linksOutput = if (urlsFound.isEmpty()) "N/A" else urlsFound.joinToString(", ")
            println("Visited: $url -> Found links: $linksOutput")

            urlsFound
                .filter { it !in visited }
                .filter { it.startsWith(domain) }
                .forEach { queue.add(it) }
        }

        return visited.toList()
    }

    private fun extractUrls(html: String, url: String): List<String> {
        val doc = Jsoup.parse(html, url)
        return doc.select("a[href]")
            .map { it.absUrl("href") }
    }
}
