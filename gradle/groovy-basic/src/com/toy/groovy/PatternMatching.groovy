package com.toy.groovy

emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$"

println "test@gmail.com" ==~ emailPattern // true
println "test" ==~ emailPattern // false

