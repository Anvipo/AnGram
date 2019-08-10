package com.anvipo.angram.coreLayer.collections

interface IMutableStack<E> : IReadOnlyStack<E> {

    fun push(item: E): E

    fun pop(): E?

}