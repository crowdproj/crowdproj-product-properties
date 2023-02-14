import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class RectangleTest : FunSpec({

    test("rectangle area") {
        val r = Rectangle(10, 20)
        r.area() shouldBe 200
        r.width shouldBe 10
        r.height shouldBe 20
    }

    test("toString") {
        val r = Rectangle(10, 20)
        r.toString() shouldBe "Rectangle(10x20)"
    }

    test("equals and hashcode") {
        val a = Rectangle(10, 20)
        val b = Rectangle(10, 20)
        val c = Rectangle(20, 10)
        a shouldBe b
        a.hashCode() shouldBe b.hashCode()
        a shouldBeEqualToComparingFields b
        a shouldNotBe c
    }

    test("square equals") {
        val a = Square(10)
        val b = Square(10)
        val c = Square(20)
        a shouldBe b
        a.hashCode() shouldBe b.hashCode()
        a shouldBeEqualToComparingFields b
        a shouldNotBe c
    }

    test("figure area") {
        var f: Figure = Rectangle(10, 20)
        f.area() shouldBe 200

        f = Square(10)
        f.area() shouldBe 100
    }

    test("diff area") {
        val a = Rectangle(10, 20)
        val b = Square(10)
        diffArea(a, b) shouldBe 100
    }

})



