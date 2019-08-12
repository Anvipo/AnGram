package com.anvipo.angram.layers.core.collections.stack

import java.util.*

class MyStack<E> : IStack<E> {

    override fun push(item: E): E {
        return _stack.push(item)
    }

    override fun pop(): E? = try {
        _stack.pop()
    } catch (ese: EmptyStackException) {
        null
    }

    override fun peek(): E? = try {
        _stack.peek()
    } catch (e: EmptyStackException) {
        null
    }

    override fun isEmpty(): Boolean {
        return _stack.empty()
    }


    override val size: Int
        get() = _stack.size

    override fun contains(element: E): Boolean {
        return _stack.contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return _stack.containsAll(elements)
    }

    override fun iterator(): MutableIterator<E> {
        return _stack.iterator()
    }


    private val _stack: Stack<E> = Stack()

}
