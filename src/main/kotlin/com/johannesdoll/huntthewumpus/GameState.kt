package com.johannesdoll.huntthewumpus

sealed class GameState {
    object Lost : GameState()
}