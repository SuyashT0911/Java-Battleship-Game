# Java Battleship Web Game 🚢

Welcome to my Java Battleship project! This is a modernized, web-based version of the classic console Battleship game, built entirely using core Java libraries for the backend and HTML/CSS/JS for the frontend.

## Features ✨

- **Vanilla Java Backend**: The game logic is powered by a custom `HttpServer` written in plain Java, without relying on heavy frameworks like Spring Boot or external dependencies.
- **Dynamic Frontend**: A responsive, neon-themed, dark-mode visual interface with animations using modern CSS and Glassmorphism.
- **Interactive Gameplay**: The board updates dynamically in real-time as you guess coordinates, keeping track of hits, misses, and the total turns taken to destroy all four ships.
- **Randomized Placement**: The hidden ships are placed randomly on the grid every time you play.
- **Docker Ready**: The repository includes a `Dockerfile` making it extremely simple to deploy to cloud providers like Render or Railway.

## How to Play Locally 💻

1. **Prerequisites**: Make sure you have [Java Development Kit (JDK 21 or higher)](https://adoptium.net/) installed on your computer.
2. Clone this repository to your local machine:
   ```bash
   git clone [your-github-repo-url]
   ```
3. Open your terminal in the project directory, and compile the `BattleshipServer.java` file:
   ```bash
   javac BattleshipServer.java
   ```
4. Run the compiled Java server:
   ```bash
   java BattleshipServer
   ```
5. Open your web browser and go to `http://localhost:8080` to start playing!

## How to Deploy Online ☁️

This project is containerized using Docker and is meant to be easily hosted on platforms like [Render](https://render.com/). To host it yourself:

1. Sign up for a free account on Render.com and select **Add New Web Service**.
2. Connect your GitHub account and select this repository.
3. Choose the **Docker** runtime option. Render will detect the `Dockerfile` and handle the build and startup commands automatically.
4. Once deployed, Render will provide a public URL you can share with your friends.

---

_Created as a portfolio project to demonstrate backend Java logic integrating with frontend web technologies._
