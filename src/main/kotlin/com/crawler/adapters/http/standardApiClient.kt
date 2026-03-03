package com.crawler.adapters.http

import com.crawler.domain.ports.CrawlerFailure
import com.crawler.domain.ports.HtmlFetcher
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class ApiHtmlFetcher(val client: HttpHandler) : HtmlFetcher {
    override fun fetch(url: String): Result<String, CrawlerFailure> =
        try {
            Success(client(Request(Method.GET, url)).bodyString())
        } catch (e: Exception) {
            Failure(CrawlerFailure(e.message ?: "Unknown error"))
        }
}