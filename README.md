# 🐯 TIGER WARS: MULTIPLAYER SURVIVAL GAME

## 📌 Overview:
- Tiger Wars is a 2D multiplayer survival game built entirely in Java. In this client-server game, players navigate a jungle map as a tiger, consuming food to increase their size and speed. To survive and dominate the leaderboard, players must strategically hunt smaller prey while avoiding larger, more dangerous tigers - both human and AI-controlled.

## ✨ Key Features:
- **Client-Server Multiplayer:** Utilizes multi-threaded Java Sockets and object streams to seamlessly synchronize the game state, positions, and interactions across multiple connected clients in real-time.
- **Dynamic AI Opponents:** Features computer-controlled tigers with intelligent behaviors; they will autonomously wander, seek food when small, flee from larger threats, and actively chase smaller prey.
- **Size-Based Combat & Survival:** Tigers grow in size as they consume food or other players; a tiger must be at least 10% larger than its target in order to successfully eat it.
- **Interactive GUI:** Includes a sleek main menu with a random name generator, a live top-5 player leaderboard, and a dynamic camera system that keeps the active player centered on the screen.
- **Responsive Controls & Graphics:** Players navigate using W, A, S, D or directional arrow keys, with the tiger sprite automatically rotating to face the current direction of movement.

## 🛠️ Tech Stack:
- **Frontend:** JavaFX for the graphical user interface, event handling, and responsive real-time animation loop.
- **Backend:** Core Java and Sockets (java.net.Socket) for structured, multi-threaded client-server communication and game state management.

## 🎮 How to Play:
1. Start the Server: Run GameServer.java to initialize the multiplayer server and host the map.
2. Launch the Client: Run GameClient.java to open the game window. (You can open multiple clients to play with friends!)
3. Choose a Name: On the main menu, type in your desired name or click "Generate" to receive a random one, then hit "PLAY".
4. Move Your Tiger: Use the W, A, S, D keys or the Arrow Keys to navigate your tiger around the jungle map.
5. Survive and Grow: Hunt down food items scattered across the map to increase your size and climb the leaderboard.
6. Hunt or Be Hunted: You can eat other tigers (both AI and real players) if you are at least 10% larger than them. Be careful—if a larger tiger catches you, it is game over!
