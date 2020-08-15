package car.model;

import java.util.ArrayList;
import java.util.Scanner;

public class MainProgramClass implements FileOperator {

	private static ArrayList<Vehicle> vehicles = new ArrayList<>();
	private static ArrayList<String> names = new ArrayList<String>();

	public static void main(String[] args) {
		
		MainProgramClass mpc = new MainProgramClass();
		
		//scanner class for choice making
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		switch(choice){
		case 0:
			mpc.saveFile(vehicles, "output.txt");
			break;
			//
		case 1:

			String[] nameOfVehicle = mpc.ArrayToArrayList(names);
			break;
		case 2:
			names= mpc.readFiles("output.txt");
		}
	}

	@Override
	public <T> void printContent(T[] t1) {

	}

	@Override
	public void saveFile(ArrayList<Vehicle> vehicles, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] ArrayToArrayList(ArrayList<String> names) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> readFiles(String path) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
// to be continue