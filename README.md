# Snake Game
This is a modified version of the popular game Snake with just some small changes. Below I've highlighted the key components and concepts I've used to create this and outlined the file structure.

## Core Concepts

  1. 2D Arrays

       I used a two 2D array to store the gridBox objects that essentially hold all the
       information about a given box during any given moment. (The 2D can be found in the Board
       class ). Each gridBox contains a final row and final column value that represents its position relative to the
       2D array gameBoard. As the game progresses, various different entities take control of any given gridBox.
       Because of this, we can check if a snake interesects food or a bomb easily by checking if they contain a reference
       to the same gridBox. This 2D array is appropriate

  2. JUnit Testable Component

       I utilized JUnit testing to test a variety of things including files being read in properly,
       files being saved properly, snake was correctly moving forwards, snake could correctly changing direction,
       finally the snake eating functionality of the bombs and food were all tested.
       I utilized edge cases such as moving into a wall or passing in an invalid board file.
       Additionally, no testing of GUI's components was utilized but rather testing the underarching functions that
       GUI components would call.

  3. File I/O

       I used the File I/O package to store the initial game state of different board
       sizes and as well as to save a current game and then be able to re-load that game.
       The files that work properly in the game include BOARD_# where # is 1-6. For each text file,
       there is a square arrangement of numbers that will eventuall lead to the gameBoard 2D array.
       Within each text file the numbers correspond to how the file should be read, 0 = open space,
       1 = snake body, 2 = head, 3 = Food, and 4 = Bomb. The number of food on the board upon start is the number of
       food on the board throughout the game (until there can't be). Text files with bomb continously grow in the number
       of bombs to make it more challenging.

  4. Inheritance

      I implemented inheritance through the usage of my Edible interface. This interface abstracts methods that both
      the Food and Bomb classes will need to utilize. The most important of which is the eat() method. This controls
      what happens when the snake intersects with the snake or the bomb and depending on what occurs there is the same output.
      However, each class has different side effects as a result of calling the eat method. I.e the bomb eat() method
      only returns the score because that is all that is needed. However, the food eat requires lots of input as the eat()
      method adds a lot of functionality to the program.

  5. Recursion

       After struggling for a while using iteration to properly read in files. I watched a youtube video
       and then I implemented a recursive depth-first search over a 2D array to mainly determine
       the correct ordering of the gridBoxes for the snakes body. This could not be done with simple
       iteration because if you try and sweep through the 2D array there are cases where you can add gridBoxes
       to the snake body before they should be added. My DFS works by starting at the snake's and then
       it scans all the grids around the head to see which is the next body part. This process then
       repeats until we reach all gridBoxes that are not snakeBody parts and are surrounded by snake body parts
       that have already been searched. During the recursion, the order in which the gridBoxes are found is stored in
       the correct order so that the snake moves properly after the file is read in.

## Implementation

### Provide an overview of each of the classes in your code, and what each class' function is in the overall game.

- runSnake - This class implements runnable and is used to run the game. It sets up the JFrame and
  instantiates and initializes the SnakeGameCourt that will control the state of the game
  until the Player dies. It handles the button pressed and creating the instruction message upon start up.

- snakeGameCourt - This class was modeled after MushRoomGameCourt class from the example game.
  It is essentially a JPanel for overall game state. I.e. it listens for the arrows being pressed to change snake direction,
  and uses tick() to make the snake move forward keep the game progressing and make the game an actual game

- Board - This class has a wide variety of functions including holding the actual 2D array used,
  as well as dealing with all the file I/O stuff including reading and writing files for the snake game.
  The file I/O methods include readBoardFile and saveBoardFile. These functions make the game run, as otherwise,
  we wouldn't have a board, food, or snake in the first place. The snakeBoxes, foodBoxes, and bombBoxes variable
  are all ArrayList of array where each array has two elements (a row and a col) sort of utilized like a tuple.
  Additionally, readBoardFile calls our Depth-First Search recursion function in order to properly read in the snake.
  These vars are filled during readBoardFile and then are used to intialize the Snake, Food, and Bomb in snakeGameCourt.
  The Board class' final important method is addFood which adds a food everytime one is eaten.

- GameObj - This class is an abstract class that is used to abstract
  the Snake and the gridBox classes. It provides basic methods for these
  classes to utilize, as well as several abstract methods uch as draw() and move().
  Every gameObj has a row and column corresponding to its position in the 2D array, a height/width (in pixels), and a
  location on the screen in pixels.

- GridBox - This class represents the Tile object. Each square on the Board is essentially
  a Tile that has given attributes that are defined after the Board class has parsed
  the given file. Several classes contain collections of Tiles. Throughout the game,
  references of the gridBoxes in the 2D array in Board are passed around the Snake, Food, and Bomb objects.

- Edible - The overarching interface for food and bomb. Defines the methods to be utilized by each of the two.
  Ideally it could have been used for my types of edible things such as like a portal that when you eat it will
  teleport the snake head or something. The main method that has different implementations is the eat method which
  executes certain code based on what was actually eaten.

- Snake - This class represents the Snake object. It contains a reference to the snake's head (a gridBox)
  that updates as the Snake moves. Additionally, it has a LinkedList of gridBoxes that includes the snake's body and head
  the reason a LinkedList instead of ArrayList is for the addFirst method which allows us to easily update the new snake head.
  It also contains a score, which is the number of foods the snake has eaten. A direction, which is an integer from 0-3 which
  represents the current direction that the Snake is moving. Finally, we store the 3 previous gridBoxes in order to easily
  add a gridBox to the snake once it has eaten a

- Food - This class represents the Food object. It is essentially just an object that holds
  an arrayList of gridBoxes that should be food. Each time that the Snake intersects with a food tile in the game,
  that gridBox is removed and then replaced with an open square. This holds the total number of food constant since
  it's a cycle of remove 1 and then add 1. Additionally it implements the Edible interface (the same as Bomb)

- Bomb - This class represents the Bomb object. It is essentially just an object that holds
  an arrayList of gridBoxes that should be bombes. Each time that the Snake intersects with a bomb tile in the game,
  eat() returns the score as the game will be ended. Additionally, it

- snakeTest - Holds all the tests for my game.
