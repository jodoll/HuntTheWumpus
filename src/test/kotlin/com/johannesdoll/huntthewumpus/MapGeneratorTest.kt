package com.johannesdoll.huntthewumpus

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.*
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot

class MapGeneratorTest : BehaviorSpec({

    Given("MapParams") {
        val mapParams = MapParams(bottomlessPits = 2)
        When("Generating a GameMap") {
            val map: GameMap = mapParams.generateMap()
            Then("The map contains an empty Room to start in") {
                map.emptyRooms() shouldNot beEmpty()
            }
            Then("The map contains a room with a Wumpus") {
                withClue("Any room should contain Wumpus") {
                    map.rooms.any { it.content == RoomContent.Wumpus } shouldBe true
                }
            }
            Then("All connected room numbers are in rooms") {
                map.rooms.map { it.number } shouldContainAll map.connectedRooms.flatMap { it.connections }.distinct()
            }
            Then("All rooms are reachable") {
                map.connectedRooms.flatMap { it.connections }.distinct() shouldContainAll map.rooms.map { it.number }
            }
            Then("Every tunnel to a room leads back, too") {
                val waysForward = map.connectedRooms
                    .map { entry -> entry.connections.map { entry.room.number to it } }
                    .flatten()
                    .distinct()

                val waysBack = waysForward.map { it.second to it.first }

                waysForward shouldContainAll waysBack
            }
        }
    }

    Given("MapParams with Giant Bats") {
        val mapParams = MapParams(giantBats = 2, bottomlessPits = 2)
        When("Generating a GameMap") {
            val map: GameMap = mapParams.generateMap()
            Then("The map contains the given amount of Giant Bats") {
                map.rooms.filter { it.content == RoomContent.GiantBat } should haveSize(2)
            }
        }
    }

    Given("MapParams with Bottomless Pits") {
        val mapParams = MapParams(bottomlessPits = 2)
        When("Generating a GameMap") {
            val map: GameMap = mapParams.generateMap()
            Then("The map contains the given amount of bottomless Pits") {
                map.rooms.filter { it.content == RoomContent.BottomlessPit } should haveSize(2)
            }
        }
    }
    Given("Shape is Square") {
        And("Size is 4 by 4") {
            val mapParams = MapParams(shape = MapShape.Rectangle(4, 4))

            When("Generating a GameMap") {
                val map: GameMap = mapParams.generateMap()
                Then("Map has 16 fields") {
                    map.rooms should haveSize(16)
                }
                Then("Room 0 is connected to Room 1") {
                    map[0].connections shouldContain 1
                }
                Then("Room 0 is connected to Room 4") {
                    map[0].connections shouldContain 4
                }
                Then("Room 0 is connected to two other Rooms") {
                    map[0].connections shouldHaveSize 2
                }
            }
        }
        And("Size is to small for an empty room to be left") {
            val mapParams = MapParams(
                giantBats = 1,
                bottomlessPits = 1,
                shape = MapShape.Rectangle(3, 1)
            )
            When("Generating a GameMap") {
                Then("generation fails") {
                    shouldThrow<IllegalArgumentException> {
                        mapParams.generateMap()
                    }
                }
            }

        }
    }
})