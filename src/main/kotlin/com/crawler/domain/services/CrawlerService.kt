package com.crawler.domain.services

import com.crawler.domain.ports.Crawler
import com.crawler.domain.ports.HtmlFetcher
import com.crawler.domain.ports.OutputPrinter
import org.jsoup.Jsoup

class CrawlerService(
    private val htmlFetcher: HtmlFetcher,
    private val outputPrinter: OutputPrinter
) : Crawler {
    override fun crawl(seedUrl: String): List<String> {
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<String>()

        queue.add(seedUrl)

        while (queue.isNotEmpty()) {
            val url = queue.removeFirst()
            if (url in visited) continue

            val html = htmlFetcher.fetch(url)
            val urlsFound = extractUrls(html, url)

            visited.add(url)

            outputPrinter.print(url, urlsFound)

            urlsFound
                .filter { it !in visited }
                .filter { it.startsWith(getDomain(seedUrl)) }
                .forEach { queue.add(it) }
        }

        return visited.toList()
    }

    private fun getDomain(seedUrl: String): String =
        "https://${seedUrl.split("/")[2]}"

    private fun extractUrls(html: String, url: String): List<String> {
        val doc = Jsoup.parse(html, url)
        return doc.select("a[href]")
            .map { it.absUrl("href") }
    }
}
