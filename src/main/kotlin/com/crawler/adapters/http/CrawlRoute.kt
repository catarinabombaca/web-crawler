package com.crawler.adapters.http

import com.crawler.domain.ports.Crawler
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.contract.meta
import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK

class CrawlRoute(val crawler: Crawler) {
    private val spec = "/crawl" meta {
        summary = "Crawl the given seed url"
        description = "The crawler should print each URL visited, and a list of links found on that page"
        receiving(crawlRequestDTOLens to crawlRequestDTOExample)
        returning(OK)
    }

    fun contractRoutes(): List<ContractRoute> =
        listOf(spec bindContract POST to ::crawlHandler)

    fun crawlHandler(request: Request): Response {
        val crawlRequestDTO = crawlRequestDTOLens(request)
        return crawler.crawl(crawlRequestDTO.url)
            .map { Response(OK) }
            .recover { Response(INTERNAL_SERVER_ERROR) }
    }

    companion object {
        val crawlRequestDTOLens = Body.auto<CrawlRequestDTO>().toLens()
        val crawlRequestDTOExample = CrawlRequestDTO(url = "https://crawlme.monzo.com")
    }
}

data class CrawlRequestDTO(val url: String)