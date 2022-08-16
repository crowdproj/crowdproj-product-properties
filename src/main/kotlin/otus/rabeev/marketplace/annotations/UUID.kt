package otus.rabeev.marketplace.annotations

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

@Constraint(validatedBy = [])
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@ReportAsSingleViolation
@Pattern(regexp = "^\\{?[0-9A-Fa-z]{8}-[0-9A-Fa-z]{4}-[0-9A-Fa-z]{4}-[0-9A-Fa-z]{4}-[0-9A-Fa-z]{12}}?$")
annotation class UUID(
    val message: String = "bad UUID",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
