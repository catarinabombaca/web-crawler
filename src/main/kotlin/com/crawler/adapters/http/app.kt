package com.crawler.adapters.http

import com.crawler.domain.services.CrawlerService
import org.http4k.client.OkHttp
import org.http4k.contract.contract
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.routing.bind
import org.http4k.routing.routes


val app: HttpHandler = routes(
    contract {
        routes += CrawlRoute(CrawlerService(OkHttp())).contractRoutes()
    }
)