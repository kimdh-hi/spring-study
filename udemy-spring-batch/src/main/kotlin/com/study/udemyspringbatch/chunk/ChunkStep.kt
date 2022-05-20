package com.study.udemyspringbatch.chunk

import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class MyItemReader: ItemReader<Int> {
    val list = listOf<Int>(1,2,3,4,5,6,7,8,9,10)

    override fun read(): Int? {
        println("Item [ Reader ] ...")
        list.forEach {
            return it
        }
        return null
    }
}

@Component
class MyItemProcessor: ItemProcessor<Int, Long> {
    override fun process(item: Int): Long? {
        println("Item [ Processor ] ...")
        return (item+1).toLong()
    }
}

@Component
class MyItemWriter: ItemWriter<Long> {
    override fun write(items: MutableList<out Long>) {
        println("Item [ Writer ] ...")
        items.forEach { println(it) }
    }
}