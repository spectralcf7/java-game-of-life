# Conway's Game of Life - using Java and Swing UI

Conway's Game of Life is a cellular automaton that is played on a 2D square grid. Each square (or "cell") on the grid can be either alive or dead, and they evolve according to the following rules (taken from https://conwaylife.com/):
  - Any live cell with fewer than two live neighbours dies (referred to as underpopulation).
  - Any live cell with more than three live neighbours dies (referred to as overpopulation).
  - Any live cell with two or three live neighbours lives, unchanged, to the next generation.
  - Any dead cell with exactly three live neighbours comes to life.

## Implementation 
### Coding Language
- Game Model and Logic: **Java**
- GUI: **Swing**

## Specifics
### The Grid
The 2D grid (along with several functionalities) is implemented using **Swing**. Aditionally, a menu bar with two options is added for the serialization/deserialization of the grid configuration: 
- Save
- Load (with grid redrawing in case the grid is resized before loading)

The grid cell status (alive or dead) is pictured as a 2D array of boolean values (**true** for alive cells, **false** for dead cells).

### Actions
An user can activate (setting its status to true) a certain cell by clicking on it. Moreover, by providing a key press listener, the user can choose from the following options: 
- Press **Enter** - Randomize the whole grid by activating a certain number of cells (using a random **Int** generator).
- Press **Spacebar** - Compute the next state by following the game rules
- Press **Backspace** - Reset the whole grid.

## Samples
Saving and Loading

https://github.com/user-attachments/assets/0ea4436a-a339-4cd8-8036-a29f3dc587da

The Cellular Automaton 

https://github.com/user-attachments/assets/991d198f-4483-44ab-aee5-2e59aa5daf67


