//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    //Calculate the total inventory value
    totalValueInventory()
    //Find the most expensive product
    findMostExpensiveProduct()
    //Check if a product named "Headphones" is in stock
    checkProductExists("Headphones")
    //Sort product
    sortProduct()
}

fun totalValueInventory(){
    var totalValue = 0.0
    for (product in products) {
        totalValue += product.price * product.quantity
    }
    totalValue = Math.round(totalValue * 100) / 100.0
    println("1. Total inventory is $totalValue")
}

fun findMostExpensiveProduct(){
    var mostExpensiveProduct: Product? = null
    var highestPrice = Double.MIN_VALUE
    for (product in products) {
        if (product.price > highestPrice) {
            highestPrice = product.price
            mostExpensiveProduct = product
        }
    }
    println("2. Most expensive product is ${mostExpensiveProduct?.name} with price is ${mostExpensiveProduct?.price}")
}

fun checkProductExists(name: String){
    var isProductInStock = false
    for (product in products) {
        if (product.name == name) {
            isProductInStock = true
            break
        }
    }
    println("3. Is products $name in stock? $isProductInStock")
}

fun sortProduct(){

    val sortedByPriceDescending = products.toMutableList()
    for (i in 0 until sortedByPriceDescending.size - 1) {
        for (j in i + 1 until sortedByPriceDescending.size) {

            if (sortedByPriceDescending[i].price < sortedByPriceDescending[j].price) {

                val temp = sortedByPriceDescending[i]
                sortedByPriceDescending[i] = sortedByPriceDescending[j]
                sortedByPriceDescending[j] = temp
            }
        }
    }

    println("4. Sort products in descending order with price:")
    for (product in sortedByPriceDescending) {
        println("${product.name}: price ${product.price}, quantity ${product.quantity}")
    }
}