package com.crawler.domain.ports

import dev.forkhandles.result4k.Result

interface HtmlFetcher {
    fun fetch(url: String): Result<String, CrawlerFailure>
}
