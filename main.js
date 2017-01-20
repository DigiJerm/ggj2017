var context;

window.onload = function()
{
	var canvas = document.getElementById("canvas");
	context = canvas.getContext("2d");
	context.fillStyle = "#ffffff";
	context.fillRect(0, 0, 800, 600);
	var line = new Line();
	context.strokeStyle = "#000000";
	context.lineWidth = 3;
	context.beginPath();
	context.moveTo(800 / (line.NumPoints - 1) * 0, 300 + line.Offsets[0]);
	for (var i = 1; i < line.NumPoints; i++)
		context.lineTo(800 / (line.NumPoints - 1) * i, 300 + line.Offsets[i]);
	context.stroke();
};



function Line()
{
	this.NumPoints = 30;
	this.Offsets = [];
	for (var i = 0; i < this.NumPoints; i++)
		this.Offsets.push(Math.sin(i / (this.NumPoints - 1) * Math.PI * 2 * 3) * 30);
}