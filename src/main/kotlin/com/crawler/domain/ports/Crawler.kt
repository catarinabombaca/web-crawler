package com.crawler.domain.ports

import dev.forkhandles.result4k.Result

interface Crawler {
    fun crawl(seedUrl: String): Result<List<String>, CrawlerFailure>
}

data class CrawlerFailure(val error: String)