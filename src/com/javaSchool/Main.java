package com.javaSchool;

import com.javaSchool.game.GameOfLife;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        GameOfLife game = new GameOfLife();
        game.startGame();
    }
}
