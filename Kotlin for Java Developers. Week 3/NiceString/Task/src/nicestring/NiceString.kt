package nicestring

fun String.isNice(): Boolean {

    fun firstCondition() =
        !this.contains("bu")
                && !this.contains("ba")
                && !this.contains("be")

    fun secondCondition() =
        this.count {
            it == 'a' ||
                    it == 'e' ||
                    it == 'i' ||
                    it == 'o' ||
                    it == 'u'
        } >= 3

    fun thirdCondition() =
        this.zipWithNext().map { it.first == it.second }.contains(true)

    return listOf(firstCondition(), secondCondition(), thirdCondition()).count { it } >= 2

}

fun main() {
    print("bac".isNice())
}