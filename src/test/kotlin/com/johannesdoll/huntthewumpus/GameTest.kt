package com.johannesdoll.huntthewumpus

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class GameTest : BehaviorSpec({
    Given("A room with a wumpus") {
        val room = Room(RoomContent.Wumpus)
        When("The player enters it") {
            val state = room.enter()
            Then("The game is lost") {
                state shouldBe GameState.Lost
            }
        }
    }
})