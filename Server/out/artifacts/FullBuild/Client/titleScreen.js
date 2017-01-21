window.onload = function()
{
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
	console.log(xhr.response);
}

function updateGameList(gameList)
{
	var template = "<ul>";
	if (gameList.length === 0)
		template += "<li>No games found"
	else {
		for (var i = 0; i < gameList.length; i++)
			template += "<li>" + gameList[i].name + " (Players: " + gameList[i].numPlayers + ")";
	}
	template += "</ul>";
	document.getElementById("gameListPanel").innerHTML = template;
}