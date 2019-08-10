package com.anvipo.angram.layers.core.collections

interface IReadOnlyStack<out E> : Collection<E> {

    fun peek(): E?

}