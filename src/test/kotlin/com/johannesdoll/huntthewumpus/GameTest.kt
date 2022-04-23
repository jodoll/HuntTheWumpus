package com.johannesdoll.huntthewumpus

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf

internal class GameTest : BehaviorSpec({
    Given("A room with a wumpus") {
        val room = Room(RoomContent.Wumpus)
        When("The player enters it") {
            val state = room.enter(Inventory())
            Then("The game is lost") {
                state should beInstanceOf<GameState.Lost>()
            }
        }
        And("The Player has arrows left") {
            val inventory = Inventory(arrows = 1)
            When("When he shoots into the room") {
                val state = room.shoot(inventory)
                Then("The game is won") {
                    state should beInstanceOf<GameState.Won>()
                }
            }
        }
    }

    Given("A room") {
        val room = Room(RoomContent.Wumpus)
        And("The player has an arrow left") {
            val inventory = Inventory(arrows = 1)
            When("The player shoots into the room") {
                val state = room.shoot(inventory = inventory)
                Then("The player has an arrow less") {
                    state.inventory.arrows shouldBe 0
                }
            }

        }
    }
})