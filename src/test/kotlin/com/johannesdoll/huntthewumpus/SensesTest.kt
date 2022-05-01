package com.johannesdoll.huntthewumpus

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should

class SensesTest : BehaviorSpec({

    Given("A room next to empty rooms") {
        val map = GameMap(
            Room(0).withTunnelsTo(1),
            Room(1).withTunnelsTo(0)
        )
        When("Sensing") {
            val result = map.rooms[0].sense(map)
            Then("Nothing is sensed") {
                result should beEmpty()
            }
        }
    }
    Given("A room next to the Wumpus") {
        val map = GameMap(
            Room(0).withTunnelsTo(1),
            Room(1, content = RoomContent.Wumpus).withTunnelsTo(0)
        )
        When("Sensing") {
            val result = map.rooms[0].sense(map)
            Then("You smell something terrible nearby") {
                result shouldContain Danger.TerribleSmell
            }
        }
    }
    Given("A room next to a bottomless Pit") {
        val map = GameMap(
            Room(0).withTunnelsTo(1),
            Room(1, content = RoomContent.BottomlessPit).withTunnelsTo(0)
        )
        When("Sensing") {
            val result = map.rooms[0].sense(map)
            Then("You feel a cold wind blowing from a nearby cavern.") {
                result shouldContain Danger.ColdWind
            }
        }
    }
    Given("A room next to a giant Bat") {
        val map = GameMap(
            Room(0).withTunnelsTo(1),
            Room(1, content = RoomContent.GiantBat).withTunnelsTo(0)
        )
        When("Sensing") {
            val result = map.rooms[0].sense(map)
            Then("You hear a rustling.") {
                result shouldContain Danger.RustlingSound
            }
        }
    }

})