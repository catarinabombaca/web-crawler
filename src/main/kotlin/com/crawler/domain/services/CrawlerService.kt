package com.crawler.domain.services

import com.crawler.domain.ports.Crawler
import com.crawler.domain.ports.HtmlFetcher
import com.crawler.domain.ports.OutputPrinter
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

class CrawlerService(
    private val htmlFetcher: HtmlFetcher,
    private val outputPrinter: OutputPrinter,
    private val concurrency: Int = 64
) : Crawler {

    override fun crawl(seedUrl: String) = runBlocking {
        val visited = ConcurrentHashMap.newKeySet<String>()
        val semaphore = Semaphore(concurrency)

        coroutineScope {
            fun launchCrawl(url: String) {
                if (!visited.add(url)) return

                launch {
                    semaphore.withPermit {
                        when (val result = withContext(Dispatchers.IO) { htmlFetcher.fetch(url) }) {
                            is Success -> {
                                val urlsFound = extractUrls(result.value, url)
                                outputPrinter.print(url, urlsFound)
                                urlsFound
                                    .filter { isSameDomain(it, seedUrl) }
                                    .forEach { launchCrawl(it) }
                            }
                            is Failure -> System.err.println("Failed to crawl $url: ${result.reason.message}")
                        }
                    }
                }
            }

            launchCrawl(seedUrl)
        }
    }

    private fun isSameDomain(url: String, seedUrl: String): Boolean =
        try { URI(url).host == URI(seedUrl).host } catch (e: Exception) { false }

    private fun extractUrls(html: String, url: String): List<String> {
        val doc = Jsoup.parse(html, url)
        return doc.select("a[href]")
            .map { it.absUrl("href") }
    }
}