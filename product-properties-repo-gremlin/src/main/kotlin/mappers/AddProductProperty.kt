package com.crowdproj.marketplace.repository.gremlin.mappers

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductPropertyLock
import com.crowdproj.marketplace.common.models.ProductUnitId
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_DELETED
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_DESCRIPTION
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_ID
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_LOCK
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_NAME
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_TMP_RESULT
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_UNITS
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_UNIT_MAIN
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.RESULT_SUCCESS
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr

fun GraphTraversal<Vertex, Vertex>.addProductProperty(productProperty: ProductProperty): GraphTraversal<Vertex, Vertex> =
    this
        .property(
            VertexProperty.Cardinality.single,
            FIELD_LOCK,
            productProperty.lock.takeIf { it != ProductPropertyLock.NONE }?.asString()
        )
        .property(VertexProperty.Cardinality.single, FIELD_NAME, productProperty.name.takeIf { it.isNotBlank() })
        .property(
            VertexProperty.Cardinality.single,
            FIELD_DESCRIPTION,
            productProperty.description.takeIf { it.isNotBlank() }
        )
        .property(
            VertexProperty.Cardinality.single,
            FIELD_UNIT_MAIN,
            productProperty.unitMain.takeIf { it != ProductUnitId.NONE }?.asString()
        )
        .property(
            VertexProperty.Cardinality.single,
            FIELD_UNITS,
            productProperty.units.filter { it != ProductUnitId.NONE }.map { it.asString() }
        )
        .property(VertexProperty.Cardinality.single, FIELD_DELETED, productProperty.deleted)

fun GraphTraversal<Vertex, Vertex>.listProductProperty(result: String = RESULT_SUCCESS): GraphTraversal<Vertex, MutableMap<String, Any>> =
    project<Any?>(
        FIELD_ID,
        FIELD_LOCK,
        FIELD_NAME,
        FIELD_DESCRIPTION,
        FIELD_UNIT_MAIN,
        FIELD_UNITS,
        FIELD_DELETED,
        FIELD_TMP_RESULT,
    )
        .by(gr.id<Vertex>())
        .by(FIELD_LOCK)
        .by(FIELD_NAME)
        .by(FIELD_DESCRIPTION)
        .by(FIELD_UNIT_MAIN)
        .by(FIELD_UNITS)
        .by(FIELD_DELETED)
        .by(gr.constant(result))

fun Map<String, Any?>.toProductProperty(): ProductProperty = ProductProperty(
    id = (this[FIELD_ID] as? String)?.let { ProductPropertyId(it) } ?: ProductPropertyId.NONE,
    lock = (this[FIELD_LOCK] as? String)?.let { ProductPropertyLock(it) } ?: ProductPropertyLock.NONE,
    name = (this[FIELD_NAME] as? String) ?: "",
    description = (this[FIELD_DESCRIPTION] as? String) ?: "",
    unitMain = (this[FIELD_UNIT_MAIN] as? String)?.let { ProductUnitId(it) } ?: ProductUnitId.NONE,
    units = (this[FIELD_UNITS] as? List<*>)?.let { anies -> anies.map { ProductUnitId(it as String) } } ?: emptyList(),
    deleted = (this[FIELD_DELETED] as? Boolean) ?: false
)