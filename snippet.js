// Find vulnerability in below code

gameState = {
    username = "Gamer42",
    score = 1445,
    timeSpent = "00:43:01"
}

serialized = JSON.stringify(gameState)

deserialized = JSON.parse(serialized)

document.getElementById("score").innerHTML = deserialized.score;
