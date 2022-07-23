package com.toy.groovy

c = { name ->
    println "hello $name"
}
c.call "kim"

10.times { n ->
    print n
}
println()
10.times {
    print it
}