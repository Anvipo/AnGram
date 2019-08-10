package com.anvipo.angram.layers.core.collections

interface IMutableStack<E> : IReadOnlyStack<E> {

    fun push(item: E): E

    fun pop(): E?

}