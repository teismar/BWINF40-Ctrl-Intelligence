package task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import util.List;

public class Datenanalyse {
	
	public Datenanalyse() {
		super();
		analysiere();
	}
	
	private double travelTime;
	private int number;
	private Hotel[] hotels;
	
	/*public static void main(String[] pArgs) {
		Datenanalyse d = new Datenanalyse();
		d.analysiere();
		//System.out.println(d.getHotels()[6].getEntfernung() + " " + d.getHotels()[6].getBewertung());
		Hotel[] folgehotels = d.getPossibleNextHotels(d.getHotels()[6]);
		
		for(int i = 0; i<folgehotels.length; i++) {
		//	System.out.println(folgehotels[i].getEntfernung() + " " + folgehotels[i].getBewertung());
		}
	}*/
	
	public double getTravelTime() {
		return travelTime;
	}

	public int getNumber() {
		return number;
	}

	public Hotel[] getHotels() {
		return hotels;
	}

	public void analysiere() {		
		try {
			File file = new File("hotels.txt");
			Scanner scanner = new Scanner(file);
			
			this.number = Integer.parseInt(scanner.nextLine());
			//System.out.println(this.number);
			this.travelTime = Double.parseDouble(scanner.nextLine());
			//System.out.println(this.travelTime);
			
			hotels = new Hotel[this.number];
			int i = 0;
			
			double letzteDistanz = this.travelTime - 360;
			
			while(scanner.hasNextLine()) {
				String[] daten = scanner.nextLine().split(" ");
				hotels[i] = new Hotel(Double.parseDouble(daten[0]), Double.parseDouble(daten[1]));
				
				if(hotels[i].getEntfernung() >= letzteDistanz) hotels[i].setKannLetztesSein(true);
				if(hotels[i].getEntfernung() <= 360) hotels[i].setKannErstesSein(true);
				
				//if(hotels[i].KannLetztesSein())System.out.println(hotels[i].getEntfernung() + " " + hotels[i].getBewertung());
				i++;
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int indexOfHotel(Hotel h) {
		int result = -1;
		
		for(int i = 0; i<this.hotels.length; i++) {
			if(hotels[i] == h) {
				result = i;
				break;
			}
		}
		
		return result;
	}
	
	public Hotel[] getPossibleNextHotels(Hotel start) {
		ArrayList<Hotel> arrListHotel = new ArrayList<Hotel>();
		double minimumStep = travelTime-(4*360);
		for(int i = 0; i<this.hotels.length; i++) {
			double distance = hotels[i].getEntfernung() - start.getEntfernung();
			
			if(distance>0 && minimumStep<=distance && distance<=360 && hotels[i] != start) {
				arrListHotel.add(hotels[i]);
			}
		}
		Hotel[] pnHotels = new Hotel[arrListHotel.size()];
		
		return arrListHotel.toArray(pnHotels);
	}
	
	public Hotel[] getFirstHotels() {
		ArrayList<Hotel> arrListHotel = new ArrayList<Hotel>();
		
		for(int i = 0; i<this.hotels.length; i++) {
			if(this.hotels[i].kannErstesSein()) arrListHotel.add(this.hotels[i]);
		}
		Hotel[] fHotels = new Hotel[arrListHotel.size()];
		
		return arrListHotel.toArray(fHotels);
	}
	
	public List<Hotel> convertArrToList(Hotel[] arrHotel) {
		List<Hotel> hList = new List<Hotel>();
		
		for(int i = 0; i<arrHotel.length; i++) {
			hList.append(arrHotel[i]);
		}
		
		hList.toFirst();
		return hList;
	}
	
}
