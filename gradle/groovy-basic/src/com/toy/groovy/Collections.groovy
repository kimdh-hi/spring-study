package com.toy.groovy

l = [1, 2, 3]
println l

l.each {print it }
println()

l.reverseEach {print it }
println()

l += [4, 5, 6]
println l

s = ["a", "b", "c"] as Set
println s
println s.class // LinkedHashSet

TreeSet ts = ["a", "b", "c"]
println ts
println ts.class // TreeSet

m = ["k1":"v1", "k2":"v2"]
println m
m.each { k, v ->
    println("key: $k, value: $v")
}
println m["k1"]
