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

	var canvas = document.getElementById("canvas");
	context = canvas.getContext("2d");

	window.addEventListener('keyup', function(event) { Key.onKeyup(event); }, false);
	window.addEventListener('keydown', function(event) { Key.onKeydown(event); }, false);

	game = new Game(gameHash, playerHash);

	game.render();
	window.setInterval(handleFrame, 50);
};

function handleFrame()
{
	game.update();
	game.render();
}