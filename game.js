function Game()
{
	this.line = new Line();
}



Game.prototype.render = function()
{
	context.strokeStyle = "#000000";
	context.lineWidth = 3;
	context.beginPath();
	context.moveTo(800 / (this.line.NumPoints - 1) * 0, 300 + this.line.Offsets[0]);
	for (var i = 1; i < this.line.NumPoints; i++)
		context.lineTo(800 / (this.line.NumPoints - 1) * i, 300 + this.line.Offsets[i]);
	context.stroke();
};