package com.crowdproj.marketplace.common.exceptions

import com.crowdproj.marketplace.common.models.ProductPropertyLock

class RepoConcurrencyException(expectedLock: ProductPropertyLock, actualLock: ProductPropertyLock?) : RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)