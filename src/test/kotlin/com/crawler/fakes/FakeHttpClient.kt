package com.crawler.fakes

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK

val exampleHtml: String = ClassLoader.getSystemResourceAsStream("example.html")!!.bufferedReader().readText()

val fakeHttpClient: HttpHandler = { request ->
    when (request.uri.toString()) {
        "https://example.com/" -> Response(OK).body(exampleHtml)
        "https://example.com/about" -> Response(OK).body("<html><body></body></html>")
        "https://example.com/contact" -> Response(OK).body("<html><body></body></html>")
        else -> Response(NOT_FOUND)
    }
}