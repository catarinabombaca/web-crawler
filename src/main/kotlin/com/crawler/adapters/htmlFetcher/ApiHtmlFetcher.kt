package com.crawler.adapters.htmlFetcher

import com.crawler.CrawlerFailure
import com.crawler.domain.ports.HtmlFetcher
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.mapFailure
import dev.forkhandles.result4k.resultFrom
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class ApiHtmlFetcher(val client: HttpHandler) : HtmlFetcher {
    override fun fetch(url: String): Result<String, CrawlerFailure> =
        resultFrom { client(Request(Method.GET, url)) }
            .mapFailure { CrawlerFailure("Network error: ${it.message}") }
            .flatMap { response ->
                if (response.status.successful) Success(response.bodyString())
                else Failure(CrawlerFailure("Server error: HTTP ${response.status.code}"))
            }
}