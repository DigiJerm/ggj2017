var context;
var game;

window.onload = function()
{
	var canvas = document.getElementById("canvas");
	context = canvas.getContext("2d");

	window.addEventListener('keyup', function(event) { Key.onKeyup(event); }, false);
	window.addEventListener('keydown', function(event) { Key.onKeydown(event); }, false);

	game = new Game();

	game.render();
	window.setInterval(handleFrame, 50);
};

function handleFrame()
{
	game.update();
	game.render();
}