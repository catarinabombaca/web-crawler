package com.crawler.adapters.htmlFetcher

import com.crawler.domain.ports.CrawlerFailure
import com.crawler.domain.ports.HtmlFetcher
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.mapFailure
import dev.forkhandles.result4k.resultFrom
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class ApiHtmlFetcher(val client: HttpHandler) : HtmlFetcher {
    override fun fetch(url: String): Result<String, CrawlerFailure> =
        resultFrom { client(Request(Method.GET, url)).bodyString() }
            .mapFailure { CrawlerFailure( "Failed to fetch HTML: ${it.message}") }
}