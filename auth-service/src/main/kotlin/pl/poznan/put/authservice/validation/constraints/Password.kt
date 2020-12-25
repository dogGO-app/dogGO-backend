package pl.poznan.put.authservice.validation.constraints

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Pattern(
        regexp = """^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,128}$""",
        message = "Password should have at least one lowercase letter, one uppercase letter, one digit " +
                "and should be 8-128 chars long."
)
@Constraint(validatedBy = [])
annotation class Password(
        val message: String = "{pl.poznan.put.authservice.validation.constraints.Password.message}",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)