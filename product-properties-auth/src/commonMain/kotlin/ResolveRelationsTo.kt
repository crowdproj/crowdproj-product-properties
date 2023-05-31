package com.crowdproj.marketplace.auth

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import com.crowdproj.marketplace.common.permissions.PropPrincipalRelations

fun ProductProperty.resolveRelationsTo(principal: PropPrincipalModel): Set<PropPrincipalRelations> = setOfNotNull(
    PropPrincipalRelations.NONE,
    PropPrincipalRelations.NEW.takeIf { id == ProductPropertyId.NONE },
    PropPrincipalRelations.OWN.takeIf { principal.id == ownerId },
    PropPrincipalRelations.PUBLIC.takeIf { principal.id != ownerId },
)