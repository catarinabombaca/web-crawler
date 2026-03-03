package com.crawler.domain.services

import com.crawler.domain.ports.Crawler
import com.crawler.domain.ports.CrawlerFailure
import com.crawler.domain.ports.HtmlFetcher
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import org.jsoup.Jsoup

class CrawlerService(
    private val htmlFetcher: HtmlFetcher,
) : Crawler {
    override fun crawl(seedUrl: String): Result<List<String>, CrawlerFailure> {
        return try {
            val domain = "https://${seedUrl.split("/")[2]}"
            val visited = mutableSetOf<String>()
            val queue = ArrayDeque<String>()

            queue.add(seedUrl)

            while (queue.isNotEmpty()) {
                val url = queue.removeFirst()
                if (url in visited) continue

                val html = htmlFetcher.fetch(url)
                val urlsFound = extractUrls(html, url)

                visited.add(url)

                val linksOutput = if (urlsFound.isEmpty()) "N/A" else urlsFound.joinToString(", ")
                println("Visited: $url -> Found links: $linksOutput")

                urlsFound
                    .filter { it !in visited }
                    .filter { it.startsWith(domain) }
                    .forEach { queue.add(it) }
            }

            Success(visited.toList())
        } catch (e: Exception) {
            Failure(CrawlerFailure(e.message ?: "Unknown error"))
        }
    }

    private fun extractUrls(html: String, url: String): List<String> {
        val doc = Jsoup.parse(html, url)
        return doc.select("a[href]")
            .map { it.absUrl("href") }
    }
}
