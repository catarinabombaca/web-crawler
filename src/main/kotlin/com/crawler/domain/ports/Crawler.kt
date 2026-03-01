package com.crawler.domain.ports

interface Crawler {
    fun crawl(seedUrl: String): List<String>
}