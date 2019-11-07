package club.sk1er.vigilance.data

import java.lang.StringBuilder
import java.util.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Property(
    val type: PropertyType,
    val name: String,
    val category: String,
    val subcategory: String = "",
    val description: String = "",
    /**
     * Reserved for [PropertyType.SLIDER]
     */
    val min: Int = 0,
    /**
     * Reserved for [PropertyType.SLIDER]
     */
    val max: Int = 0,
    /**
     * Reserved for [PropertyType.DROP_DOWN]
     */
    val options: Array<String>
)

fun Property.fullPropertyPath(): String {
    val sb = StringBuilder()

    sb.append(category.toPropertyPath()).append(".")

    if (subcategory != "") {
        sb.append(subcategory.toPropertyPath()).append(".")
    }

    sb.append(name.toPropertyPath())

    return sb.toString()
}

private fun String.toPropertyPath() = this.toLowerCase(Locale.ENGLISH).replace(" ", "_")