package com.crowdproj.marketplace.app.controller

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1ProductProperty() {
    route("product") {
        post("property/create") {
            call.createProductProperty()
        }
        post("properties/read") {
            call.readProductProperties()
        }
        post("property/update") {
            call.updateProductProperty()
        }
        post("property/delete") {
            call.deleteProductProperty()
        }
        post("property/search") {
            call.searchProductProperty()
        }
    }
}