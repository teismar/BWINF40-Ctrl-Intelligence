package task;

import java.util.Stack;

import util.*;

public class Routenplanung {

	public static void main(String[] args) {
		List<Hotel> tempListHotels = new List<Hotel>();
		List<ArbitraryTree<Hotel>> tempListTrees = new List<ArbitraryTree<Hotel>>();
		
		//Der Einfachheit merken, welche Knoten in welcher Tiefe auftauchen:
		List<ArbitraryTree<Hotel>> depth1 = new List<ArbitraryTree<Hotel>>();
		List<ArbitraryTree<Hotel>> depth2 = new List<ArbitraryTree<Hotel>>();
		List<ArbitraryTree<Hotel>> depth3 = new List<ArbitraryTree<Hotel>>();
		List<ArbitraryTree<Hotel>> depth4 = new List<ArbitraryTree<Hotel>>();
		
		List<ArbitraryTree<Hotel>> blätter = new List<ArbitraryTree<Hotel>>();
		
		Datenanalyse d = new Datenanalyse();
		ArbitraryTree<Hotel> start = new ArbitraryTree<Hotel>(new Hotel(0, Double.MAX_VALUE)); //Startwurzel des Baumes, Tiefe 0
		
		//Baum aufbauen, mögliche Starthotels als Array in Liste überführen und als Nachfolger des Startknotens festlegen, maximale Tiefe von 4, da nur 4 hotels besucht werden können, um innerhalb von 5 Tagen am Ziel zu sein.
		//Wichtig ist, dass die Hotels mehrmals im Baum auftauchen. Daher müssen nebenbei die Blätter gespeichert werden, die rechtzeitig zum Ziel führen.
		//Tiefe 1:
		tempListHotels = d.convertArrToList(d.getFirstHotels());
		
		tempListHotels.toFirst();
		tempListTrees.toFirst();
		while(tempListHotels.hasAccess()) {
			ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
			tempListTrees.append(tree);
			depth1.append(tree);
			tempListHotels.next();
		}
		
		start.setSuccessors(tempListTrees);
		
		
		//Tiefe 2:
		depth1.toFirst();
		while(depth1.hasAccess()) {
			tempListTrees = null;
			tempListHotels = null;
			
			if(depth1.getContent().getContent().kannLetztesSein() == false) {
				tempListHotels = d.convertArrToList(d.getPossibleNextHotels(depth1.getContent().getContent()));
				tempListHotels.toFirst();
				tempListTrees = new List<ArbitraryTree<Hotel>>();
				
				while(tempListHotels.hasAccess()) {
					ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
					tempListTrees.append(tree);
					depth2.append(tree);
					tempListHotels.next();
				}
				depth1.getContent().setSuccessors(tempListTrees);
			}else if(depth1.getContent().getContent().kannLetztesSein() == true){
				blätter.append(depth1.getContent());
			}
			
			depth1.next();
		}
		depth1.toFirst();
		while(!depth1.isEmpty()) {
			depth1.remove();
		}
		depth1 = null;
		
		
		//Tiefe 3:
		depth2.toFirst();
		while(depth2.hasAccess()) {
			tempListTrees = null;
			tempListHotels = null;
			
			if(depth2.getContent().getContent().kannLetztesSein() == false) {
				tempListHotels = d.convertArrToList(d.getPossibleNextHotels(depth2.getContent().getContent()));
				tempListHotels.toFirst();
				tempListTrees = new List<ArbitraryTree<Hotel>>();
				
				while(tempListHotels.hasAccess()) {
					ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
					tempListTrees.append(tree);
					depth3.append(tree);
					tempListHotels.next();
				}
				depth2.getContent().setSuccessors(tempListTrees);
			}else if(depth2.getContent().getContent().kannLetztesSein() == false){
				blätter.append(depth2.getContent());
			}			
			depth2.next();
		}
		depth2.toFirst();
		while(!depth2.isEmpty()) {
			depth2.remove();
		}
		depth2 = null;
		
		//Tiefe 4:
		depth3.toFirst();
		while(depth3.hasAccess()) {
			tempListTrees = null;
			tempListHotels = null;
			
			if(depth3.getContent().getContent().kannLetztesSein() == false) {
				tempListHotels = d.convertArrToList(d.getPossibleNextHotels(depth3.getContent().getContent()));
				tempListHotels.toFirst();
				tempListTrees = new List<ArbitraryTree<Hotel>>();
				
				while(tempListHotels.hasAccess()) {
					ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
					tempListTrees.append(tree);
					depth4.append(tree);
					tempListHotels.next();
				}
				depth3.getContent().setSuccessors(tempListTrees);
			}else if(depth3.getContent().getContent().kannLetztesSein() == true){
				blätter.append(depth3.getContent());
			}			
			depth3.next();
		}
		depth3.toFirst();
		while(!depth3.isEmpty()) {
			depth3.remove();
		}
		depth3 = null;
		
		//Durchsuchen von depth4 nach fertigen Routen. Sie sind fertig, wenn man vom Inhaltsobjekt des Knotens direkt zum Ziel kommt.
		depth4.toFirst();
		while(depth4.hasAccess()) {
			if(depth4.getContent().getContent().kannLetztesSein() == true) blätter.append(depth4.getContent());
			depth4.next();
		}
		depth4.toFirst();
		while(!depth4.isEmpty()) {
			depth4.remove();
		}
		depth4 = null;
		
		
		//Jetzt wird geschaut, welche Route wie gut ist. Um das Arbeiten zu vereinfachen, wird die Liste in einen Array überführt.
		blätter.toFirst();
		int i = 0;
		while(blätter.hasAccess()) {
			i++;
			blätter.next();
		}
		blätter.toFirst();
		
		int j = 0;
		
		ArbitraryTree<Hotel>[] arrBlätter = (ArbitraryTree<Hotel>[]) new ArbitraryTree[i];
		while(blätter.hasAccess()) {
			arrBlätter[j] = blätter.getContent();
			blätter.next();
			j++;
		}
		
		
		//Vom Blatt aus müssen nun die Vorgänger identifiziert werden, um die Qualität dieser Route zu bewerten.
		ArbitraryTree<Hotel> vorgänger;
		int indexBesteBewertung = 0;
		double besteBewertung = 0;
		for(int k = 0; k<arrBlätter.length; k++) {
			vorgänger = arrBlätter[k].getPredecessor();
			double bewertung = arrBlätter[k].getContent().getBewertung();
			while(vorgänger!=null) {
				if(vorgänger.getContent().getBewertung()<bewertung) bewertung = vorgänger.getContent().getBewertung();
				vorgänger = vorgänger.getPredecessor();
			}
			if(bewertung>besteBewertung) {
				besteBewertung = bewertung;
				indexBesteBewertung = k;
			}
		}
		
		
		//Nun wurde ein möglicher, nach der Aufgabenstellung, idealer Weg gefunden.
		//Jetzt wird mit Hilfe eines Stacks vom Blatt ausgehend die Route aus den Vorgängern ermittelt.
		//Der Stack korrigiert dabei die verkehrte Reihenfolge beim Auslesen.
		Stack<Hotel> stackHotel = new Stack<Hotel>();
		ArbitraryTree<Hotel> blatt = null;
		if(arrBlätter.length>0) blatt = arrBlätter[indexBesteBewertung];
		else {
			System.out.println("Es gibt keine funktionierende Route.");
			System.exit(0);
		}
		
		stackHotel.push(blatt.getContent());
		
		vorgänger = blatt.getPredecessor();
		while(vorgänger!=null) {
			stackHotel.push(vorgänger.getContent());
			vorgänger = vorgänger.getPredecessor();
		}
		
		//Startwurzel entfernen:
		stackHotel.pop();
		
		//Somit kann jetzt im finalen Schritt die Route ausgelesen werden, wobei der Stack geleert wird.
		System.out.print("Start --> ");
		while(!stackHotel.isEmpty()) {
			System.out.print("Hotel bei " + stackHotel.peek().getEntfernung() + "min mit Bewertung " + stackHotel.peek().getBewertung() + " --> ");
			stackHotel.pop();
		}
		System.out.println("Ziel");
		System.out.println("Schlechteste Bewertung: " + besteBewertung);
	}	
}
