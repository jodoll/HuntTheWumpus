package com.johannesdoll.huntthewumpus

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.collections.shouldContainAll
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
})