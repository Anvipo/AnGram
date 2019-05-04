package com.anvipo.angram.coreLayer.collections

interface IReadOnlyStack<out E> : Collection<E> {

    fun peek(): E?

}