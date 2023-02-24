package org.redrune.utility

import io.github.classgraph.ClassGraph

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since May 02, 2020
 */
object ReflectionUtils {

    val result = ClassGraph().enableClassInfo().scan()

    /**
     * Finds all subclasses of the parameterized type, and stores them into the returned list
     */
    inline fun <reified T> findSubclasses(): ArrayList<T> {
        val name = T::class.qualifiedName
        val list = result.getSubclasses(name).loadClasses() as MutableList<Class<*>>?
        val classes = ArrayList<T>()
        list?.forEach { classes.add(it.newInstance() as T) }
        return classes
    }

    /**
     * Finds all subclasses of the parameterized type, and stores them into the returned list
     */
    inline fun <reified T> findImplementations(): ArrayList<T> {
        val name = T::class.qualifiedName
        val list = result.getClassesImplementing(name).loadClasses() as MutableList<Class<*>>?
        val classes = ArrayList<T>()
        list?.forEach { classes.add(it.newInstance() as T) }
        return classes
    }

    inline fun <reified T> getChildClassesOf(): MutableList<T> {
        val kClass = T::class
        val name = kClass.qualifiedName
        val result = ClassGraph().enableClassInfo().blacklistClasses(name).scan()
        val classes = mutableListOf<T>()
        result.use { result ->
            val subclasses = result.getSubclasses(name)
            subclasses.forEach {
                val clazz = result.loadClass(it.name, true).newInstance() as T
                classes.add(clazz)
            }
        }
        return classes
    }

}