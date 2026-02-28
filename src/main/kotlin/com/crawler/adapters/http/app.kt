package com.crawler.adapters.http

import org.http4k.contract.contract
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.routing.bind
import org.http4k.routing.routes


val app: HttpHandler = routes(
    "/ping" bind GET to ::healthHandler,
    "/formats/json/jackson" bind GET to ::formatHandler,
    contract {
        routes += CrawlRoute().contractRoutes()
    }
)