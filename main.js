var context;

window.onload = function()
{
	var canvas = document.getElementById("canvas");
	context = canvas.getContext("2d");
	context.fillStyle = "green";
	context.fillRect(0, 0, 800, 600);
};