package club.sk1er.vigilance.data

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.any
import strikt.assertions.filter
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class AnnotationParsingTest {
    private val categories = ExampleConfig.getCategories()

    init {
        println(categories.joinToString(separator = "\n"))
    }

    @Test
    fun `There are the correct number of categories`() {
        expectThat(categories).hasSize(2)
    }

    @Test
    fun `Categories with name "Cat1" and "Cat2" exist`() {
        expectThat(categories).any { get { name }.isEqualTo("Cat1") }
        expectThat(categories).any { get { name }.isEqualTo("Cat2") }
    }

    @Test
    fun `Category "Cat1" has the correct items`() {
        val items = categories.first { it.name == "Cat1" }.items

        expectThat(items).hasSize(4)
        expectThat(items).filter { it is DividerItem }.hasSize(2)
        expectThat(items).filter { it is PropertyItem }.hasSize(2)
    }

    @Test
    fun `Category "Cat2" has the correct items`() {
        val items = categories.first { it.name == "Cat2" }.items

        expectThat(items).hasSize(3)
        expectThat(items).filter { it is DividerItem }.hasSize(1)
        expectThat(items).filter { it is PropertyItem }.hasSize(2)
    }
}