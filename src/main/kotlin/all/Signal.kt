package all

class Signal<T> {
    class Connection

    private val callbacks = mutableMapOf<Connection, (T) -> Unit>()

    fun emit(newValue: T) {
        callbacks
            .values
            .forEach { it(newValue) }
    }

    fun connect(callback: (newValue: T) -> Unit): Connection {
        val connection = Connection()
        callbacks[connection] = callback
        return connection
    }

    fun disconnect(connection: Connection) {
        callbacks -= connection
    }
}
