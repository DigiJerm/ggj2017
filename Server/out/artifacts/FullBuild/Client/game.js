function Game()
{
	this.line = new Line();
}



Game.prototype.update = function()
{
	if (Key.isDown(Key.LEFT)) {
		var temp = this.line.Offsets[0];
		for (var i = 0; i < this.line.NumPoints - 1; i++)
			this.line.Offsets[i] = this.line.Offsets[i + 1];
		this.line.Offsets[this.line.NumPoints - 1] = temp;
	}
};



Game.prototype.render = function()
{
	// clear background
	context.fillStyle = "#ffffff";
	context.fillRect(0, 0, 800, 600);

	// draw line
	context.strokeStyle = "#000000";
	context.lineWidth = 3;
	context.beginPath();
	context.moveTo(800 / (this.line.NumPoints - 1) * 0, 300 + this.line.Offsets[0]);
	for (var i = 1; i < this.line.NumPoints; i++)
		context.lineTo(800 / (this.line.NumPoints - 1) * i, 300 + this.line.Offsets[i]);
	context.stroke();
};