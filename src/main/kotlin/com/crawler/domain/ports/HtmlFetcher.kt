package com.crawler.domain.ports

import com.crawler.CrawlerFailure
import dev.forkhandles.result4k.Result

interface HtmlFetcher {
    fun fetch(url: String): Result<String, CrawlerFailure>
}