package plugin.command

import kotlin.reflect.KClass

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 5/31/2017
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandManifest(
    /**
     * The description of the command
     */
    val description: String = "",
    /**
     * The parameter types of the command
     */
    val types: Array<KClass<*>> = []
)