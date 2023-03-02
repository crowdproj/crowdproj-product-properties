package com.crowdproj.marketplace.mappers.v1.exceptions

import com.crowdproj.marketplace.common.models.PropCommand

class UnknownPropCommand(command: PropCommand) : Exception("Wrong command $command at mapping toTransport stage")