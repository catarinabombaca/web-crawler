package com.crawler.domain.services

import com.crawler.domain.ports.Crawler
import com.crawler.domain.ports.HtmlFetcher
import com.crawler.domain.ports.OutputPrinter
import dev.forkhandles.result4k.onFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.concurrent.ConcurrentHashMap

class CrawlerService(
    private val htmlFetcher: HtmlFetcher,
    private val outputPrinter: OutputPrinter,
    private val parallelFetches: Int = 64
) : Crawler {

    override fun crawl(seedUrl: String) = runBlocking {
        val visited = ConcurrentHashMap.newKeySet<String>()
        val semaphore = Semaphore(parallelFetches)

        coroutineScope {
            fun launchCrawl(url: String) {
                val normalisedUrl = normaliseUrl(url)
                if (!visited.add(normalisedUrl)) return

                launch {
                    semaphore.withPermit {
                        val html = withContext(Dispatchers.IO) { htmlFetcher.fetch(normalisedUrl) }
                            .onFailure {
                                System.err.println("Failed to crawl $normalisedUrl: ${it.reason.message}")
                                return@withPermit
                            }

                        val urlsFound = extractUrls(html, normalisedUrl)
                        outputPrinter.print(normalisedUrl, urlsFound)
                        urlsFound
                            .filter { isSameDomain(it, seedUrl) }
                            .forEach { launchCrawl(it) }
                    }
                }
            }

            launchCrawl(seedUrl)
        }
    }

    private fun normaliseUrl(url: String): String =
        url.trimEnd('/').let { trimmed ->
            if ('/' in trimmed.substringAfter("://")) trimmed else url
        }

    private fun isSameDomain(url: String, seedUrl: String): Boolean =
        url.substringAfter("://").substringBefore("/") ==
        seedUrl.substringAfter("://").substringBefore("/")

    private fun extractUrls(html: String, url: String): List<String> {
        val doc = Jsoup.parse(html, url)
        return doc.select("a[href]")
            .map { it.absUrl("href") }
    }
}