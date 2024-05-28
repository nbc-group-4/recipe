package nbc.group.recipes

import dagger.Binds
import dagger.Module
import javax.inject.Inject


class Repository @Inject constructor(
    source: Resource
) {


}

interface Resource {

}

class ResourceImpl: Resource {

}

fun main() {

}

class Zoo() {

    val dog = Dog()
    val cat = Cat()

    fun sounds() {
        listOf(dog, cat).forEach {
            println(it.sound())
        }
    }
}

class Dog(): Animal {
    override fun sound() {
        println("멍멍")
    }
}

class Cat(): Animal {
    override fun sound() {
        println("냐옹")
    }
}

interface Animal {
    fun sound()
}