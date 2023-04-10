package com.crowdproj.marketplace.app.controller

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1ProductProperty() {
    route("product") {
        route("property") {
            post("create") {
                call.createProductProperty()
            }
            post("update") {
                call.updateProductProperty()
            }
            post("delete") {
                call.deleteProductProperty()
            }
            post("search") {
                call.searchProductProperty()
            }
        }
        route("properties") {
            post("read") {
                call.readProductProperties()
            }
        }
    }
}