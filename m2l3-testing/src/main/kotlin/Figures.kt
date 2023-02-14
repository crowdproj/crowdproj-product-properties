interface Figure {
    fun area(): Int
}

data class Rectangle(
    val width: Int,
    val height: Int,
) : Figure {
    override fun area() = width * height

    override fun toString(): String = "Rectangle(${width}x$height)"
}

data class Square(
    val width: Int,
) : Figure {
    override fun area() = width * width
}

fun diffArea(figure1: Figure, figure2: Figure) = figure1.area() - figure2.area()