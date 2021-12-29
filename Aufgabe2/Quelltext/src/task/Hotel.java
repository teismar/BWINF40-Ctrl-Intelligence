package task;

public class Hotel {
	
	private double entfernung;
	private double bewertung;
	private boolean kannLetztesSein = false;
	private boolean kannErstesSein = false;
	
	public Hotel(double entfernung, double bewertung) {
		this.entfernung = entfernung;
		this.bewertung = bewertung;
	}

	public double getEntfernung() {
		return entfernung;
	}

	public double getBewertung() {
		return bewertung;
	}

	public boolean kannLetztesSein() {
		return kannLetztesSein;
	}

	public void setKannLetztesSein(boolean kannLetztesSein) {
		this.kannLetztesSein = kannLetztesSein;
	}

	public boolean kannErstesSein() {
		return kannErstesSein;
	}

	public void setKannErstesSein(boolean kannErstesSein) {
		this.kannErstesSein = kannErstesSein;
	}
}
