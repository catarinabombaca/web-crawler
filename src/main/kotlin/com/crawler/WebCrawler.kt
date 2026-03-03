package com.crawler

import com.crawler.adapters.htmlFetcher.ApiHtmlFetcher
import com.crawler.adapters.printer.TerminalPrinter
import com.crawler.domain.services.CrawlerService
import org.http4k.client.OkHttp

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: web-crawler <url>")
        return
    }

    val seedUrl = args[0]
    val crawler = CrawlerService(ApiHtmlFetcher(OkHttp()), TerminalPrinter())

    try {
        crawler.crawl(seedUrl)
    } catch (e: Exception) {
        System.err.println("Error: ${e.message}")
    }
}
