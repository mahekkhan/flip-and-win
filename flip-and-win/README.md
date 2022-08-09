## Description of the game and our game design 

“Flip-and-Win” 
- The server for the game can be started by any player. All players (including the player who started the session) connect to that server as clients. 
- The shared object is the 4x4 gameboard of buttons which “locks” that object for concurrency; i.e., only one player at a time can click a button on the board. 
- Must have at least 2 players to play.
- Everyone shares a 4x4 game board, which has a random number between 1 to 100 hidden behind each button.
- Everyone takes turns clicking or “flipping” a button to uncover the randomized hidden number.
- When a button is clicked, the hidden number behind the button gets added to each player’s total points. As points are added, messages are displayed in the server’s terminal and the player’s current total number score is showcased in the client’s terminal.
- After n rounds, whoever has the highest total points wins the game. This highest score is displayed in the server terminal. 
- The game displays a message to both the winner and the other player in the game board if they won or not.

## Game design

Details
- Language used: Java.
- Used TCP protocol to handle socket connection.
- Used JFrame and JButton to create a grid for the game board.

Classes
Client class:
- Generate grid for each Player.
- Handle Player connection with Server.
- Handle Player activities and information.
- Handles the shared object locking and unlocking.

Server class:
- Handle Server connection with each Player.
- Generate hidden values in the grid.
- Keep track of the game’s progress and status.

## Workflow
- Run the Server, Server will generate player id, maximum number of turns to play, and hidden values for the grid and waiting for Player to connect and to send this information.
- Run the Player, each Player will generate their own grid, and receive their id, maximum number of turns they can play, and hidden value of each tile of the grid from the Server.
- Each Player will take turns to play, click on a tile, then send the number of the clicked tile to the Server, Server will inform other Players that tile has been clicked.
- Clicked tiles will show their hidden value, and will be disabled for all Players.
- Server will keep track of the point of each Player - the sum of all the hidden values that each Player has clicked.
- After all the tiles have been clicked - max turns to play is reached - Server will show the winner on the terminal, Player will show the ending message on the grid.
