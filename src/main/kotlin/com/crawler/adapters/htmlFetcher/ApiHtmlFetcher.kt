package com.crawler.adapters.htmlFetcher

import com.crawler.domain.ports.HtmlFetcher
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class ApiHtmlFetcher(val client: HttpHandler) : HtmlFetcher {
    override fun fetch(url: String): String =
        try {
            client(Request(Method.GET, url)).bodyString()
        } catch (e: Exception) {
            throw RuntimeException("Failed to fetch HTML: ${e.message}", e)
        }
}
