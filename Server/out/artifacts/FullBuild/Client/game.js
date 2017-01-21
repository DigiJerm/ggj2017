function Game(gameHash)
{
	this.gameHash = gameHash;
	this.line = new Line();
	this.charge = 0;
}



Game.prototype.update = function()
{
	if (Key.isDown(Key.SPACE)) {
		this.charge++;
	} else {
		this.charge = 0;
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

	// draw charger
	if (this.charge !== 0) {
		context.beginPath();
		context.rect(30, 100 - this.charge * 3, 70, this.charge * 3);
		context.fillStyle = "#20f060";
		context.fill();
	}
};