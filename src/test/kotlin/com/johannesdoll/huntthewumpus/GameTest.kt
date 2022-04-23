package com.johannesdoll.huntthewumpus

import arrow.core.Either
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf

internal class GameTest : BehaviorSpec({
    val initialState = GameState.Idle(
        inventory = Inventory(),
        currentRoom = Room(0)
    )

    Given("A room with a wumpus") {
        val room = Room(content = RoomContent.Wumpus)
        When("The player enters it") {
            val state = room.enter(initialState)
            Then("The game is lost") {
                state should beInstanceOf<GameState.Lost>()
            }
        }
        And("The Player has arrows left") {
            val inventory = Inventory(arrows = 1)
            When("When he shoots into the room") {
                val result = room.shoot(initialState.withInventory(inventory))
                Then("The game is won") {
                    result.state should beInstanceOf<GameState.Won>()
                }
            }
        }
    }

    Given("A room") {
        val room = Room(roomNumber = 1, content = RoomContent.Wumpus)
        And("The player has an arrow left") {
            val inventory = Inventory(arrows = 1)
            When("The player shoots into the room") {
                val result = room.shoot(initialState.withInventory(inventory))
                Then("The player has an arrow less") {
                    result.state.inventory.arrows shouldBe 0
                }
                Then("Current room does not change") {
                    result.state.currentRoom shouldBe initialState.currentRoom
                }
            }
        }
        And("The player is out of arrows") {
            val inventory = Inventory(arrows = 0)
            When("The player shoots into the room") {
                val result = room.shoot(initialState.withInventory(inventory))
                Then("It fails") {
                    result should beInstanceOf<Either.Left<Unit>>()
                }
                Then("Game State doesn't change") {
                    result.state should beInstanceOf<GameState.Idle>()
                }
            }
        }
        When("The player enters the room") {
            val state = room.enter(initialState)
            Then("Current room changes to that room") {
                state.currentRoom shouldBe room
            }
        }
    }

    Given("An empty room") {
        val room = Room(content = RoomContent.Empty)
        When("The player enters the room") {
            val state = room.enter(initialState)
            Then("Game state is Idle") {
                state should beInstanceOf<GameState.Idle>()
            }
        }
        And("The player has arrows left") {
            val inventory = Inventory(arrows = 1)
            When("The player shoots into the room") {
                val result = room.shoot(initialState.withInventory(inventory))
                Then("Game state is Idle") {
                    result.state should beInstanceOf<GameState.Idle>()
                }
            }
        }
    }
})