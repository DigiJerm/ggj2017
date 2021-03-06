function Game(gameHash, playerHash, playerIndex)
{
	this.gameHash = gameHash;
	this.playerHash = playerHash;
	this.playerIndex = playerIndex;
	this.line = new Line();
	this.charge = 0;
	this.gameEnded = false;

	this.chargeAnimation = [];
	this.backgroundImage = preLoadImage("Assets/Background.png");
	for (var i = 0; i < 18; i++) {
		var image = new Image();
		image.src = "Assets/ExplosionImplosionAnimation/" + (i + 1) + ".png";
		this.chargeAnimation.push(image);
	}
	this.upArrowImage = preLoadImage("Assets/indicatorUp.png");
	this.downArrowImage = preLoadImage("Assets/indicatorDown.png");
	this.winImage = preLoadImage("Assets/ExplosionImplosionIconWin.png");
	this.loseImage = preLoadImage("Assets/ExplosionImplosionIconLose.png");
	preLoadEnd();
}



Game.prototype.update = function()
{
	//if (this.gameEnded)
		//return;
	this.loadState();
	if (Key.isDown(Key.UP)) {
		if (this.charge < 45) {
			this.charge++;
			this.sendCharge();
		}
	} else {
		if (this.charge > 0) {
			this.charge--;
			this.sendCharge();
		}
	}
	if (Key.isDown(Key.DOWN)) {
		if (this.charge > -45) {
			this.charge--;
			this.sendCharge();
		}
	} else {
		if (this.charge < 0) {
			this.charge++;
			this.sendCharge();
		}
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
		context.lineTo(1920 / (this.line.NumPoints - 1) * i, 450 - this.line.Offsets[i]);
	context.stroke();
	context.shadowColor = "black";
	context.shadowBlur = 0;

	// draw charger
	var maxFrame = 2;
	const arrowSize = 160;
	if (this.charge !== 0) {
		maxFrame = Math.floor((Math.abs(this.charge) - 1) / 3) + 3;
		if (this.playerIndex === 0)
			context.drawImage(this.charge > 0 ? this.upArrowImage : this.downArrowImage, 480 - arrowSize / 2, 140 - arrowSize / 2, arrowSize, arrowSize);
		else
			context.drawImage(this.charge > 0 ? this.upArrowImage : this.downArrowImage, 1920 - 480 - arrowSize / 2, 900 - 140 - arrowSize / 2, arrowSize, arrowSize);
	}
	const chargeSize = 250;
	for (var i = maxFrame; i >= 0; i--) {
		if (this.playerIndex === 0)
			context.drawImage(this.chargeAnimation[i], 419, 0, 2213, 2213, 253 - chargeSize / 2, 140 - chargeSize / 2, chargeSize, chargeSize);
		else
			context.drawImage(this.chargeAnimation[i], 419, 0, 2213, 2213, 1920 - 253 - chargeSize / 2, 900 - 140 - chargeSize / 2, chargeSize, chargeSize);
	}

	if (this.gameEnded) {
		context.drawImage(this.win ? this.winImage : this.loseImage, 339, 0, 1241, 900);
	}
};



Game.prototype.sendCharge = function() {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function(x) {
		return function() {
			if (x.readyState == XMLHttpRequest.DONE) {
				if (x.status !== 200)
					console.log("Error :( Can't send charge to server :(");
			}
		};
	}(xhr);
	xhr.open("PUT", "/game/submit/" + this.gameHash + "/" + this.playerHash);
	xhr.responseType = "json";
	xhr.send(JSON.stringify({ action: "charge", amount: this.charge }));
};



Game.prototype.pulse = function() {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function(x) {
		return function() {
			if (x.readyState == XMLHttpRequest.DONE) {
				if (x.status !== 200)
					console.log("Error :( Can't send data to server :(");
			}
		};
	}(xhr);
	xhr.open("PUT", "/game/submit/" + this.gameHash + "/" + this.playerHash);
	xhr.responseType = "json";
	xhr.send(JSON.stringify({ action: "pulse", amount: this.charge }));
};



Game.prototype.loadState = function () {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function(x, g) {
		return function() {
			if (x.readyState == XMLHttpRequest.DONE) {
				if (x.status === 200)
					g.handleStateLoaded(x);
				else
					console.log("Error :( Can't load data from server :(");
			}
		};
	}(xhr, this);
	xhr.open("GET", "/load/" + this.gameHash);
	xhr.responseType = "json";
	xhr.send();
};



Game.prototype.handleStateLoaded = function (xhr) {
	for (var i = 0; i < xhr.response.gamePlayers.length; i++) {
		if (xhr.response.gamePlayers[i].isDead) {
			this.gameEnded = true;
			this.win = (i === this.playerIndex);
		}
	}
	for (var i = 0; i < xhr.response.lines[0].lineMagnitude.length; i++) {
		this.line.Offsets[i] = xhr.response.lines[0].lineMagnitude[i];
	}
};