
class Product(
    val name: String,
    val price: Double,
    val quantity: Int
)

val products = listOf(
    Product("Laptop", 999.99, 5),
    Product("Smartphone", 499.99, 10),
    Product("Tablet", 299.99, 0),
    Product("Headphones", 199.99, 3)
)
