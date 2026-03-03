package com.crawler.adapters.http

import com.crawler.domain.ports.HtmlFetcher
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class apiHtmlFetcher(val client: HttpHandler) : HtmlFetcher {
    override fun fetch(url: String): String {
        return client(Request(Method.GET, url)).bodyString()
    }
}