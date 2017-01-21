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
	var template = "<ul>";
	if (gameList.length === 0)
		template += "<li>No games found"
	else {
		for (var i = 0; i < gameList.gameData.length; i++) {
			var queryString = "gameHash=" + gameList.gameData[i].gameHash + "&playerHash=" + gameList.gameData[i].gamePlayers[1] + "&playerIndex=1";
			template += "<li><a href='game.html?" + queryString + "'>Game " + (i + 1) + "</a>";
		}
	}
	template += "</ul>";
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
	xhr.send(JSON.stringify({ playerCount: 2, gameWidth: 30, gameHeight: 30 }));
}

function handleGameCreated(xhr)
{
	window.location = "game.html?gameHash=" + xhr.response.gameHash + "&playerHash=" + xhr.response.gamePlayers[0] + "&playerIndex=0";
}