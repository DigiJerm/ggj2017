function Game(gameHash, playerHash)
{
	this.gameHash = gameHash;
	this.playerHash = playerHash;
	this.line = new Line();
	this.charge = 0;
	this.chargeAnimation = [];
	this.backgroundImage = new Image();
	this.backgroundImage.src = "Assets/Background.png";
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
	context.fillRect(0, 0, 1920, 900);
	context.globalAlpha = 0.8;
	context.drawImage(this.backgroundImage, 0, 0, 1920, 900);
	context.globalAlpha = 1.0;

	// draw conduit
	context.strokeStyle = "#DD1E08";
	context.lineWidth = 3;
	context.shadowColor = "black";
	context.shadowBlur = 8;
	context.beginPath();
	context.moveTo(1920 / (this.line.NumPoints - 1) * 0, 450 + this.line.Offsets[0]);
	for (var i = 1; i < this.line.NumPoints; i++)
		context.lineTo(1920 / (this.line.NumPoints - 1) * i, 450 + this.line.Offsets[i]);
	context.stroke();
	context.shadowColor = "black";
	context.shadowBlur = 0;

	// draw charger
	var maxFrame = 2;
	if (this.charge !== 0)
		maxFrame = Math.floor((this.charge - 1) / 3) + 3;
	for (var i = maxFrame; i >= 0; i--)
		context.drawImage(this.chargeAnimation[i], 419, 0, 2213, 2213, 148, 4, 210, 210);
};