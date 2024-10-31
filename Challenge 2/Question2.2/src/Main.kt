//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val arr = intArrayOf(3, 7, 1, 2, 6, 4)
    val missingNumber = findMissingNumber(arr)
    println("The missing number is: $missingNumber")
}

fun findMissingNumber(array: IntArray): Int {

    val n = array.size
    var sumAllNumbers = 0
    var sumArray = 0

    for (i in 1 .. (n + 1))
        sumAllNumbers += i

    for (i in 0 until n)
        sumArray += array[i]

    return sumAllNumbers - sumArray
}