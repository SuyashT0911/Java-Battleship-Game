const gridElement = document.getElementById('grid');
const turnsElement = document.getElementById('turns');
const shipsLeftElement = document.getElementById('ships-left');
const messageBox = document.getElementById('message-box');
const restartBtn = document.getElementById('restart-btn');

let gameOver = false;

function initDisplay() {
    gridElement.innerHTML = '';
    gameOver = false;
    turnsElement.innerText = '0';
    shipsLeftElement.innerText = '4';
    messageBox.innerText = 'System ready. Awaiting coordinates.';
    messageBox.style.color = 'var(--text)';
    restartBtn.classList.add('hidden');

    for (let r = 0; r < 4; r++) {
        for (let c = 0; c < 4; c++) {
            const cell = document.createElement('div');
            cell.classList.add('cell');
            cell.dataset.row = r;
            cell.dataset.col = c;
            cell.addEventListener('click', () => makeGuess(r, c, cell));
            gridElement.appendChild(cell);
        }
    }
}

async function startGame() {
    await fetch('/api/start', { method: 'POST' });
    initDisplay();
}

async function makeGuess(r, c, cellElement) {
    if (gameOver) return;
    if (cellElement.classList.contains('hit') || cellElement.classList.contains('miss')) return;

    const response = await fetch('/api/guess', {
        method: 'POST',
        body: `${r},${c}`
    });
    const data = await response.json();

    if (data.alreadyGuessed) return;

    turnsElement.innerText = data.turns;
    
    if (data.hit) {
        cellElement.classList.add('hit');
        cellElement.innerText = '×';
        shipsLeftElement.innerText = 4 - data.totalHits;
        messageBox.innerText = 'Target Hit!';
        messageBox.style.color = 'var(--hit)';
    } else {
        cellElement.classList.add('miss');
        cellElement.innerText = '○';
        messageBox.innerText = 'Target Missed.';
        messageBox.style.color = 'var(--miss)';
    }

    if (data.gameOver) {
        gameOver = true;
        messageBox.innerText = `Victory! All ships destroyed in ${data.turns} turns.`;
        messageBox.style.color = 'var(--primary)';
        restartBtn.classList.remove('hidden');
    }
}

restartBtn.addEventListener('click', startGame);

// Start on load
startGame();
