window.onload = function()
{
	document.getElementById("createGameBtn").addEventListener("click", createGame);
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function(x) {
		return function() {
			if (x.readyState == XMLHttpRequest.DONE) {
				if (x.status == 200)
					handleGameList(x);
				else {
					alert("Error :( Can't get game list :(");
					updateGameList([]);
				}
			}
		};
	}(xhr);
	xhr.open("GET", "/gameList");
	xhr.responseType = "json";
	xhr.send();
};

function handleGameList(xhr)
{
	updateGameList(xhr.response);
}

function updateGameList(gameList)
{
	var template = "";
	var numGames = 0;
	if (gameList.gameData !== undefined && gameList.gameData.length !== 0) {
		for (var i = 0; i < gameList.gameData.length; i++) {
			var finished = false;
			for (var j = 0; j < gameList.gameData[i].gamePlayers.length; j++) {
				if (gameList.gameData[i].gamePlayers[j].isDead) {
					finished = true;
					break;
				}
			}
			if (finished)
				continue;
			var queryString = "gameHash=" + gameList.gameData[i].gameHash + "&playerHash=" + gameList.gameData[i].lines[0].linePlayer1 + "&playerIndex=1";
			template += "<a href='game.html?" + queryString + "'>Game " + (numGames + 1) + "</a><br>";
			numGames++;
		}
	}
	if (numGames === 0)
		template += "No games found";
	document.getElementById("gameListPanel").innerHTML = template;
}

function createGame()
{
	//var gameName = document.getElementById("gameNameTxt").value;

	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function(x) {
		return function() {
			if (x.readyState == XMLHttpRequest.DONE) {
				if (x.status == 200)
					handleGameCreated(x);
				else
					alert("Error :( Can't create game :(");
			}
		};
	}(xhr);
	xhr.open("POST", "/create/submit");
	xhr.responseType = "json";
	xhr.send(JSON.stringify({ playerCount: 2, gameWidth: 500, gameHeight: 900 }));
}

function handleGameCreated(xhr)
{
	window.location = "game.html?gameHash=" + xhr.response.gameHash + "&playerHash=" + xhr.response.lines[0].linePlayer0 + "&playerIndex=0";
}