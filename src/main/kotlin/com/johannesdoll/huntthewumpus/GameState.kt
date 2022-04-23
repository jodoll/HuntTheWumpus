package com.johannesdoll.huntthewumpus

sealed class GameState {
    object Lost : GameState()
    object Won : GameState()
}