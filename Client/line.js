function Line()
{
	this.NumPoints = 30;
	this.Offsets = [];
	for (var i = 0; i < this.NumPoints; i++)
		this.Offsets.push(Math.sin(i / (this.NumPoints - 1) * Math.PI * 2 * 3) * 30);
}