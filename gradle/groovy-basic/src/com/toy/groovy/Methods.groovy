package com.toy.groovy

def foo(x, y = 10) {
    x + y
}
result = foo 1, 2
println result

println foo(1)


void justPrint(Map map) {
    println map.name
    println map.age
}
justPrint(["name": "kim", "age": 27])