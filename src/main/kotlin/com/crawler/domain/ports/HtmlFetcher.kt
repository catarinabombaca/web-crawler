package com.crawler.domain.ports

interface HtmlFetcher {
    fun fetch(url: String): String
}