package game.entity.item

import java.io.Serializable

/**
 * Container class.
 *
 * @author Graham / edited by Dragonkk(Alex)
 */
class ItemsContainer<T : Item>(size: Int, alwaysStackable: Boolean) : Serializable {

    var items: Array<Item?>
        private set
    private var alwaysStackable = false

    fun goesOverAmount(container: ItemsContainer<T>): Boolean {
        for (i in 0 until container.size) {
            val item: Item? = container[i]
            if (item != null) {
                if (getNumberOf(item) + item.getAmount() < 0) {
                    return true
                }
            }
        }
        return false
    }

    val size: Int
        get() = items.size

    operator fun get(slot: Int): T? {
        return if (slot < 0 || slot >= items.size) {
            null
        } else items[slot] as T?
    }

    fun getNumberOf(item: Item): Int {
        var count = 0
        for (aData in items) {
            if (aData != null) {
                if (aData.id == item.id) {
                    count += aData.getAmount()
                }
            }
        }
        return count
    }

    fun shift() {
        val oldData = items
        items = arrayOfNulls(oldData.size)
        var ptr = 0
        for (i in items.indices) {
            if (oldData[i] != null) {
                items[ptr++] = oldData[i]
            }
        }
    }

    fun forceAdd(item: T): Boolean {
        for (i in items.indices) {
            if (items[i] == null) {
                items[i] = item
                return true
            }
        }
        return false
    }

    fun remove(item: T): Int {
        var removed = 0
        var toRemove = item!!.getAmount()
        for (i in items.indices) {
            if (items[i] != null) {
                if (items[i]!!.id == item.id) {
                    var amt = items[i]!!.getAmount()
                    if (amt > toRemove) {
                        removed += toRemove
                        amt -= toRemove
                        toRemove = 0
                        items[i] = Item(items[i]!!.id, amt)
                        return removed
                    } else {
                        removed += amt
                        toRemove -= amt
                        items[i] = null
                    }
                }
            }
        }
        return removed
    }

    fun removeAll(item: T) {
        for (i in items.indices) {
            if (items[i] != null) {
                if (items[i]!!.id == item!!.id) {
                    items[i] = null
                }
            }
        }
    }

    fun containsOne(item: T): Boolean {
        for (aData in items) {
            if (aData != null) {
                if (aData.id == item!!.id) {
                    return true
                }
            }
        }
        return false
    }

    operator fun contains(item: T): Boolean {
        var amtOf = 0
        for (aData in items) {
            if (aData != null) {
                if (aData.id == item!!.id) {
                    amtOf += aData.getAmount()
                }
            }
        }
        return amtOf >= item!!.getAmount()
    }

    fun clear() {
        for (i in items.indices) {
            items[i] = null
        }
    }

    val freeSlots: Int
        get() {
            var s = 0
            for (aData in items) {
                if (aData == null) {
                    s++
                }
            }
            return s
        }
    val usedSlots: Int
        get() {
            var s = 0
            for (aData in items) {
                if (aData != null) {
                    s++
                }
            }
            return s
        }

    fun getNumberOf(item: Int): Int {
        var count = 0
        for (aData in items) {
            if (aData != null) {
                if (aData.id == item) {
                    count += aData.getAmount()
                }
            }
        }
        return count
    }

    val itemsCopy: Array<Item?>
        get() {
            val newData = arrayOfNulls<Item>(
                items.size
            )
            System.arraycopy(items, 0, newData, 0, newData.size)
            return newData
        }

    fun asItemContainer(): ItemsContainer<Item> {
        val c = ItemsContainer<Item>(
            items.size, alwaysStackable
        )
        System.arraycopy(items, 0, c.items, 0, items.size)
        return c
    }

    fun getThisItemSlot(item: T): Int {
        for (i in items.indices) {
            if (items[i] != null) {
                if (items[i]!!.id == item!!.id) {
                    return i
                }
            }
        }
        return freeSlot
    }

    val freeSlot: Int
        get() {
            for (i in items.indices) {
                if (items[i] == null) {
                    return i
                }
            }
            return -1
        }

    fun lookup(id: Int): Item? {
        for (aData in items) {
            if (aData == null) {
                continue
            }
            if (aData.id == id) {
                return aData
            }
        }
        return null
    }

    fun lookupSlot(id: Int): Int {
        for (i in items.indices) {
            if (items[i] == null) {
                continue
            }
            if (items[i]!!.id == id) {
                return i
            }
        }
        return -1
    }

    fun reset() {
        items = arrayOfNulls(items.size)
    }

    fun remove(preferredSlot: Int, item: Item): Int {
        var removed = 0
        var toRemove = item.getAmount()
        if (items[preferredSlot] != null) {
            if (items[preferredSlot]!!.id == item.id) {
                var amt = items[preferredSlot]!!.getAmount()
                if (amt > toRemove) {
                    removed += toRemove
                    amt -= toRemove
                    toRemove = 0
                    // data[preferredSlot] = new
                    // Item(data[preferredSlot].getDefinition().getIds(), amt);
                    set2(preferredSlot, Item(items[preferredSlot]!!.id, amt))
                    return removed
                } else {
                    removed += amt
                    toRemove -= amt
                    // data[preferredSlot] = null;
                    set(preferredSlot, null)
                }
            }
        }
        for (i in items.indices) {
            if (items[i] != null) {
                if (items[i]!!.id == item.id) {
                    var amt = items[i]!!.getAmount()
                    if (amt > toRemove) {
                        removed += toRemove
                        amt -= toRemove
                        toRemove = 0
                        // data[i] = new Item(data[i].getDefinition().getIds(),
                        // amt);
                        set2(i, Item(items[i]!!.id, amt))
                        return removed
                    } else {
                        removed += amt
                        toRemove -= amt
                        // data[i] = null;
                        set(i, null)
                    }
                }
            }
        }
        return removed
    }

    fun set2(slot: Int, item: Item?) {
        if (slot < 0 || slot >= items.size) {
            return
        }
        items[slot] = item
    }

    operator fun set(slot: Int, item: T?) {
        if (slot < 0 || slot >= items.size) {
            return
        }
        items[slot] = item
    }

    fun addAll(container: ItemsContainer<T>) {
        for (i in 0 until container.size) {
            val item = container[i]
            if (item != null) {
                add(item)
            }
        }
    }

    fun add(item: T): Boolean {
        if (alwaysStackable || item!!.definitions.isStackable || item.definitions.isNoted) {
            for (i in items.indices) {
                if (items[i] != null) {
                    if (items[i]!!.id == item!!.id) {
                        items[i] = Item(
                            items[i]!!.id, items[i]!!.getAmount() + item.getAmount()
                        )
                        return true
                    }
                }
            }
        } else {
            if (item.getAmount() > 1) {
                return if (freeSlots() >= item.getAmount()) {
                    for (i in 0 until item.getAmount()) {
                        val index = freeSlot()
                        items[index] = Item(item.id, 1)
                    }
                    true
                } else {
                    false
                }
            }
        }
        val index = freeSlot()
        if (index == -1) {
            return false
        }
        items[index] = item
        return true
    }

    fun freeSlots(): Int {
        var j = 0
        for (aData in items) {
            if (aData == null) {
                j++
            }
        }
        return j
    }

    fun freeSlot(): Int {
        for (i in items.indices) {
            if (items[i] == null) {
                return i
            }
        }
        return -1
    }

    fun hasSpaceFor(item: Item): Boolean {
        return hasSpaceForItem(item as T)
    }

    fun hasSpaceFor(container: ItemsContainer<T>): Boolean {
        for (i in 0 until container.size) {
            val item = container[i]
            if (item != null) {
                if (!hasSpaceForItem(item)) {
                    return false
                }
            }
        }
        return true
    }

    fun hasSpaceForItem(item: T): Boolean {
        if (alwaysStackable || item!!.definitions.isStackable || item.definitions.isNoted) {
            for (aData in items) {
                if (aData != null) {
                    if (aData.id == item!!.id) {
                        return true
                    }
                }
            }
        } else {
            if (item.getAmount() > 1) {
                return freeSlots() >= item.getAmount()
            }
        }
        val index = freeSlot()
        return index != -1
    }

    fun toArray(): Array<Item?> {
        return items
    }

    fun canAdd(item: T): Boolean {
        if (alwaysStackable || item!!.definitions.isStackable || item.definitions.isNoted) {
            for (aData in items) {
                if (aData != null) {
                    if (aData.id == item!!.id) {
                        return true
                    }
                }
            }
        } else {
            if (item.getAmount() > 1) {
                return freeSlots() >= item.getAmount()
            }
        }
        val index = freeSlot()
        return index != -1
    }

    override fun toString(): String {
        var contents = ""
        for ((index, item) in items.withIndex()) {
            contents += "$index to Item(${item?.id}, ${item?.amount})\n"
        }
        return contents
    }

    companion object {
        private const val serialVersionUID = 1099313426737026107L
    }

    init {
        items = arrayOfNulls(size)
        this.alwaysStackable = alwaysStackable
    }
}