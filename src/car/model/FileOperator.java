package car.model;

import java.util.ArrayList;

public interface FileOperator {

	<T> void printContent(T[] t1);
	void saveFile(ArrayList<Vehicle> vehicles, String path);
	abstract ArrayList<String>readFiles(String path);
	
	String [] ArrayToArrayList(ArrayList<String>names);
}
