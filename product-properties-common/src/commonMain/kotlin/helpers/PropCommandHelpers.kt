package com.crowdproj.marketplace.common.helpers

import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropCommand

fun PropContext.isUpdatableCommand() =
    this.command in listOf(PropCommand.CREATE, PropCommand.UPDATE, PropCommand.DELETE)