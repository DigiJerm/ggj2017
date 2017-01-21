var context;
var game;

// http://stackoverflow.com/a/5158301
function getParameterByName(name)
{
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

window.onload = function()
{
	var gameHash = parseInt(getParameterByName("gameHash"));
	var playerHash = parseInt(getParameterByName("playerHash"));
	var playerIndex = parseInt(getParameterByName("playerIndex"));

	var canvas = document.getElementById("canvas");
	context = canvas.getContext("2d");

	window.addEventListener('keyup', function(event) { Key.onKeyup(event); }, false);
	window.addEventListener('keydown', function(event) { Key.onKeydown(event); }, false);

	game = new Game(gameHash, playerHash, playerIndex);
};

function handleFrame()
{
	game.update();
	game.render();
}



var numImages = 0;
var numLoadedImages = 0;
var preLoadEnded = false;
var gameRunning = false;

function preLoadImage(url)
{
	var img = new Image();
	numImages++;
	img.onload = function() {
		numLoadedImages++;
		imageLoaded();
	};
	img.src = url;
	return img;
}



function preLoadEnd()
{
	preLoadEnded = true;
	imageLoaded();
}



function imageLoaded()
{
	if (!preLoadEnded)
		return;
	if (numLoadedImages === numImages)
	{
		gameRunning = true;
		document.getElementById("loadingOverlay").style.display = "none";
		document.getElementById("canvas").style.display = "block";
		startGame();
	}
}



function startGame()
{
	game.render();
	window.setInterval(handleFrame, 50);
}