package com.johannesdoll.huntthewumpus

import arrow.core.NonEmptyList

fun MapParams.generateMap(): GameMap {
    val minSize = giantBats + bottomlessPits + 2
    require(shape.numberOfRooms > minSize) { "Map has only ${shape.numberOfRooms} of the required $minSize rooms" }

    val roomNumbers = (0 until shape.numberOfRooms).asSequence().shuffled()

    val rooms = roomNumbers
        .mapIndexed { index, number ->
            val content = when {
                index < giantBats -> RoomContent.GiantBat
                index < giantBats + bottomlessPits -> RoomContent.BottomlessPit
                index < giantBats + bottomlessPits + 1 -> RoomContent.Wumpus
                else -> RoomContent.Empty
            }

            Room(number, content)
        }
        .sortedBy { it.number }
        .toList()
        .let { shape.layOutRooms(it) }
        .toList()

    return GameMap(rooms)
}

fun <T> List<T>.toNelUnsafe() = NonEmptyList.fromListUnsafe(this)