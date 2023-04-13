package com.crowdproj.marketplace.biz

import PropStub
import com.crowdproj.marketplace.common.PropContext

class ProductPropertyProcessor {
    suspend fun exec(context: PropContext) {
        context.propertyResponse = PropStub.get()
        context.propertiesResponse = PropStub.getList()
    }
}