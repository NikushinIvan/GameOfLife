package com.javaSchool.game;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class GameOfLife {
    private final char DEAD = '-';
    private int width = 20;

    private int height = 10;
    private char alive = '*';
    private boolean endGame = false;
    private int aliveCell = 30;
    private char[][] gameField;
    private char[][] oldField;
    private String osName;

    public GameOfLife() {
        osName = System.getProperty("os.name");

        fieldInitialization();
    }


    public void startGame() throws InterruptedException, IOException {
        while (!endGame) {
            viewField();
            nextGeneration();
            Thread.sleep(400);
        }
    }

    private void viewField() throws IOException, InterruptedException {
        if (osName.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            new ProcessBuilder("/bin/bash", "-c", "clear").inheritIO().start().waitFor();

        }
        for (char[] row: gameField) {
            for (char cell: row) {
                System.out.print(cell);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void nextGeneration() {
        char[][] newField = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int aliveNeighbors = getNumberAliveNeighbors(i, j);
                if (gameField[i][j] == alive) {
                    if (aliveNeighbors < 2 || aliveNeighbors > 3) {
                        newField[i][j] = DEAD;
                        --aliveCell;
                    } else {
                        newField[i][j] = gameField[i][j];
                    }
                } else {
                    if (aliveNeighbors == 3) {
                        newField[i][j] = alive;
                        ++aliveCell;
                    } else {
                        newField[i][j] = gameField[i][j];
                    }
                }
            }
        }
        if ((Arrays.deepEquals(newField, gameField)) || (aliveCell == 0) || (Arrays.deepEquals(newField, oldField))) {
            endGame = true;
        } else {
            oldField = gameField;
            gameField = newField;
        }
    }

    private int getNumberAliveNeighbors(int y, int x) {
        int aliveNeighbors = 0;
        int startX = Math.max((x - 1), 0);
        int startY = Math.max((y - 1), 0);
        int stopX = (x == width-1) ? x : x+1;
        int stopY = (y == height-1) ? y : y+1;
        for (int h = startY; h <= stopY; h++) {
            for (int w = startX; w <= stopX; w++) {
                if (h != y || w != x) {
                    if (gameField[h][w] == alive) {
                        ++aliveNeighbors;
                    }
                }
            }
        }
        return aliveNeighbors;
    }

    private void fieldInitialization() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите символ для обозначения живой ячейки: ");
        setAliveChar(scanner.nextLine());

        System.out.print("Введите ширину игрового поля (>3): ");
        setWidth(scanner.nextLine());

        System.out.print("Введите высоту игрового поля (>3): ");
        setHeight(scanner.nextLine());

        gameField = new char[height][width];
        for (char[] row : gameField) {
            Arrays.fill(row, DEAD);
        }

        System.out.print("Введите стартовое число живых клеток: ");
        setAliveCell(scanner.nextLine());

        int ac = aliveCell;
        while (ac > 0) {
            int x = (int) (Math.random()*(height-1));
            int y = (int) (Math.random()*(width-1));

            if (gameField[x][y] != alive) {
                gameField[x][y] = alive;
                --ac;
            }
        }
    }

    private void setAliveChar(String line) {
        if (line.length() > 1) {
            System.out.println("Введено больше одного символа. Принят '"+line.charAt(0)+"'\n");
            alive = line.charAt(0);
        } else if (line.length() == 1) {
            alive = line.charAt(0);
        } else {
            System.out.println("Не введен ни один символ. Используется символ по умолчанию '*'\n");
        }
    }

    private void setWidth(String line) {
        try {
            int num = Integer.parseInt(line);
            if (num < 4) {
                throw new RuntimeException();
            }
            width = num;
        } catch (Exception e) {
            System.out.println("Введено недопустимое значение. Ширина по умолчанию 20\n");
        }
    }

    private void setHeight(String line) {
        try {
            int num = Integer.parseInt(line);
            if (num < 4) {
                throw new RuntimeException();
            }
            height = num;
        } catch (Exception e) {
            System.out.println("Введено недопустимое значение. Высота по умолчанию 20\n");
        }
    }

    private void setAliveCell(String line) {
        try {
            int num = Integer.parseInt(line);
            if (num < 4 || num > height*width) {
                throw new RuntimeException();
            }
            aliveCell = num;
        } catch (Exception e) {
            System.out.println("Введено недопустимое значение. Число клеток по умолчанию 20\n");
        }
    }
}
