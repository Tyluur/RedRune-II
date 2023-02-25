package org.redrune.utility

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

/**
 * @author GregHib <greg@gregs.world>
 * @author Tyluur <itstyluur@icloud.com>
 * @since March 26, 2020
 */

inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
): T = getKoin().get(qualifier, parameters)

fun getBoolProperty(key: String): Boolean =
    getKoin().getProperty<String>(key)!!.toString().lowercase(Locale.getDefault()) == "true"

fun getIntProperty(key: String): Int = getKoin().getProperty<String>(key)!!.toInt()

fun getProperty(key: String): String = getKoin().getProperty(key)!!

fun getFloatProperty(key: String): Float = getKoin().getProperty(key)!!

fun getIntProperty(key: String, defaultValue: Int): Int =
    getKoin().getProperty<String>(key)?.toIntOrNull() ?: defaultValue

inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
): Lazy<T> = getKoin().inject(qualifier, parameters = parameters)

inline fun <reified S, reified P> bind(
        noinline parameters: ParametersDefinition? = null,
): S = getKoin().bind<S, P>(parameters)