package com.johannesdoll.huntthewumpus

import arrow.core.Either
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.types.beInstanceOf

internal class GameTest : BehaviorSpec({
    val gameMap = mapOf(
        Room(2) to setOf(Room(1)),
        Room(1) to setOf(Room(2))
    )
    val initialState = GameState.Idle(
        inventory = Inventory(),
        currentRoom = Room(1),
        map = GameMap(gameMap),
        random = PseudoRandomGameLogic()
    )

    Given("A room with a wumpus") {
        val wumpusRoom = Room(content = RoomContent.Wumpus)
        When("The player enters it") {
            val state = wumpusRoom.enter(initialState)
            Then("The game is lost") {
                state should beInstanceOf<GameState.Lost>()
            }
        }
        And("The Player has arrows left") {
            val inventory = Inventory(arrows = 1)
            When("When he shoots into the room") {
                val result = wumpusRoom.shootInto(initialState.withInventory(inventory))
                Then("The game is won") {
                    result.state should beInstanceOf<GameState.Won>()
                }
            }
            When("He shoots into a room only connected to that room") {
                val room = Room(1)
                val map = GameMap(
                    room to setOf(wumpusRoom),
                    wumpusRoom to setOf(),
                )
                val result = room.shootInto(initialState.copy(map = map).withInventory(inventory))
                Then("The game is won") {
                    result.state should beInstanceOf<GameState.Won>()
                }
            }
            When("He shoots into a room three rooms away from that room") {
                And("The arrow finds it's way to the wumpus room") {
                    val room = Room(1)
                    val map = GameMap(
                        room to setOf(Room(2)),
                        Room(2) to setOf(wumpusRoom),
                        wumpusRoom to setOf(),
                    )
                    val result = room.shootInto(initialState.copy(map = map).withInventory(inventory))
                    Then("The game is won") {
                        result.state should beInstanceOf<GameState.Won>()
                    }
                }
            }
            When("He shoots into a room four rooms away from that room") {
                And("The arrow finds it's way to the wumpus room") {
                    val room = Room(1)
                    val map = GameMap(
                        room to setOf(Room(2)),
                        Room(2) to setOf(Room(3)),
                        Room(3) to setOf(wumpusRoom),
                        wumpusRoom to setOf(),
                    )
                    val result = room.shootInto(initialState.copy(map = map).withInventory(inventory))
                    Then("The game is not won") {
                        result.state shouldNot beInstanceOf<GameState.Won>()
                    }
                }
            }
            When("He shoots into a room connected to the wumpus room and another one") {
                And("The arrow continues into the other room") {
                    val room = Room(1)
                    val testState = initialState.copy(
                        map = GameMap(
                            room to setOf(wumpusRoom, Room(2)),
                            Room(2) to setOf(),
                            wumpusRoom to setOf(),
                        ),
                        inventory = inventory,
                        random = SpoofedRandomGameLogic(2)
                    ).withInventory(inventory)
                    val result = room.shootInto(testState)
                    Then("The game is not won") {
                        result.state shouldNot beInstanceOf<GameState.Won>()
                    }
                }
            }
        }
    }

    Given("A room with a bottomless pit") {
        val room = Room(content = RoomContent.BottomlessPit)
        When("The player enters it") {
            val state = room.enter(initialState)
            Then("The game is lost") {
                state should beInstanceOf<GameState.Lost>()
            }
        }
    }

    Given("A room with a giant bat") {
        val room = Room(content = RoomContent.GiantBat)
        When("The player enters it") {
            val state = room.enter(initialState)
            Then("The game is not lost") {
                state shouldNot beInstanceOf<GameState.Lost>()
            }
            Then("The player is moved to an empty room") {
                state.currentRoom.content should beInstanceOf<RoomContent.Empty>()
            }
            Then("The the player is in a room of the map") {
                state.map.keys shouldContain state.currentRoom
            }
        }
        And("The next random room is empty") {
            val testState = initialState.copy(
                map = GameMap(
                    Room(1, content = RoomContent.Wumpus) to emptySet(),
                    Room(0, content = RoomContent.Empty) to emptySet()
                ),
                random = SpoofedRandomGameLogic(0)
            )
            When("The player enters the room with the giant Bat") {
                val state = room.enter(testState)
                Then("The player is moved to that room") {
                    state.currentRoom.number shouldBe 0
                }
            }
        }
        And("The next random room is not empty") {
            val testState = initialState.copy(
                map = GameMap(
                    Room(1, content = RoomContent.Wumpus) to emptySet(),
                    Room(0, content = RoomContent.Empty) to emptySet()
                ),
                random = PseudoRandomGameLogic()
            )
            When("The player enters the room with the giant Bat") {
                val state = room.enter(testState)
                Then("The player is moved to an empty room") {
                    state.currentRoom.content should beInstanceOf<RoomContent.Empty>()
                }
            }
        }
    }

    Given("A room") {
        val room = Room(number = 1, content = RoomContent.Wumpus)
        And("The player has an arrow left") {
            val inventory = Inventory(arrows = 1)
            When("The player shoots into the room") {
                val result = room.shootInto(initialState.withInventory(inventory))
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
                val result = room.shootInto(initialState.withInventory(inventory))
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
                val result = room.shootInto(initialState.withInventory(inventory))
                Then("Game state is Idle") {
                    result.state should beInstanceOf<GameState.Idle>()
                }
            }
        }
    }
})