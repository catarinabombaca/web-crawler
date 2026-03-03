package com.crawler.domain.ports

interface OutputPrinter {
    fun print(url: String, links: List<String>)
}
