package com.anvipo.angram.layers.global

class OrderedChat(
    val order: Long,
    val chatId: Long
) : Comparable<OrderedChat> {

    override fun compareTo(other: OrderedChat): Int {
        if (this.order != other.order) {
            return if (other.order < this.order) -1 else 1
        }

        return if (this.chatId != other.chatId) {
            if (other.chatId < this.chatId) -1 else 1
        } else {
            0
        }
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? OrderedChat

        return this.order == o?.order && this.chatId == o.chatId
    }

}