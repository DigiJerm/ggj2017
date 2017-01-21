function Game(gameHash)
{
	this.gameHash = gameHash;
	this.line = new Line();
	this.charge = 0;
	this.chargeAnimation = [];
	for (var i = 0; i < 18; i++) {
		var image = new Image();
		image.src = "Assets/ExplosionImplosionAnimation/" + (i + 1) + ".png";
		this.chargeAnimation.push(image);
	}
}



Game.prototype.update = function()
{
	if (Key.isDown(Key.SPACE)) {
		if (this.charge < 45)
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
	var maxFrame = 2;
	if (this.charge !== 0)
		maxFrame = Math.floor((this.charge - 1) / 3) + 3;
	for (var i = maxFrame; i >= 0; i--)
		context.drawImage(this.chargeAnimation[i], 419, 0, 2213, 2213, 30, 30, 150, 150);
};