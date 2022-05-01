package com.johannesdoll.huntthewumpus


data class MapParams(
    val giantBats: Int = 2,
    val bottomlessPits: Int = 2,
    val shape: MapShape = MapShape.Rectangle(5, 5)
)

sealed interface MapShape {
    val numberOfRooms: Int

    fun layOutRooms(rooms: List<Room>): List<ConnectedRoom>

    data class Rectangle(
        val length: Int,
        val width: Int
    ) : MapShape {
        override val numberOfRooms: Int
            get() = length * width

        override fun layOutRooms(rooms: List<Room>): List<ConnectedRoom> = rooms.map { room ->
            val number = room.number
            room.withTunnelsTo(
                listOfNotNull(
                    (number - 1).takeUnless { it < 0 },
                    (number - width).takeUnless { it < 0 },
                    (number + 1).takeIf { rooms.size > it },
                    (number + width).takeIf { rooms.size > it },
                ).toNelUnsafe()
            )
        }
    }
}
