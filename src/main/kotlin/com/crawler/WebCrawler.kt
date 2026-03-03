package com.crawler

import com.crawler.adapters.htmlFetcher.ApiHtmlFetcher
import com.crawler.adapters.printer.TerminalPrinter
import com.crawler.domain.services.CrawlerService
import dev.forkhandles.result4k.onFailure
import org.http4k.client.OkHttp

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: web-crawler <url>")
        return
    }

    val seedUrl = args[0]
    val crawler = CrawlerService(ApiHtmlFetcher(OkHttp()), TerminalPrinter())

    crawler.crawl(seedUrl).onFailure {
        System.err.println("Error: ${it.reason.error}")
        return
    }
}
