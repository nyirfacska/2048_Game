# 2048 Game
Java implementation of the 2048 game. This is my project for Programming Environment
and Programming Technologies classes.

## Rules
2048 is played on a gray 4×4 grid, with numbered tiles that 
slide smoothly when a player moves them using the four arrow keys. 
Every turn, a new tile will randomly appear in an empty spot on the 
board with a value of either 2 or 4. Tiles slide as far as possible 
in the chosen direction until they are stopped by either another 
tile or the edge of the grid. If two tiles of the same number 
collide while moving, they will merge into a tile with the total 
value of the two tiles that collided. The resulting tile cannot 
merge with another tile again in the same move. 

## Install
 1. `mvn clean install` on the main project
 2. `mvn exec:java` to run the game

## License
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)