package com.anvipo.angram.layers.core.collections.stack

interface IMutableStack<E> {

    fun push(item: E): E

    fun pop(): E?

}