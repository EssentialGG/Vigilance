package gg.essential.vigilance.data

open class SortingBehavior {
    /**
     * @return a comparator, that is used to sort a list of categories
     */
    open fun getCategoryComparator(): Comparator<in Category> = compareBy { it.name }

    /**
     * @return a comparator, that is used to sort a map of subcategories
     * Key of an entry = name of the subcategory
     * Value of an entry = list of properties in the subcategory
     */
    open fun getSubcategoryComparator(): Comparator<in Map.Entry<String, List<PropertyData>>> = compareBy { it.key }

    /**
     * @return a comparator, that is used to sort a list of properties
     */
    open fun getPropertyComparator(): Comparator<in PropertyData> = compareBy { it.attributes.subcategory }
}
