package car.model;

import java.time.LocalDateTime;

public class Vehicle {

	// instance variables
	private int totalVehicleInstantiated = 0;
	private String name;
	private LocalDateTime dateOfManufacturing;
	private int vehicleId;

	public Vehicle(int totalVehicleInstantiated, String name, LocalDateTime dateOfManufacturing, int id) {

		this.totalVehicleInstantiated = totalVehicleInstantiated;
		this.name = name;
		this.dateOfManufacturing = dateOfManufacturing;
		this.vehicleId = id;
		
	}

	public int getTotalVehicleInstantiated() {
		return totalVehicleInstantiated;
	}

	public void setTotalVehicleInstantiated(int totalVehicleInstantiated) {
		this.totalVehicleInstantiated = totalVehicleInstantiated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDateOfManufacturing() {
		return dateOfManufacturing;
	}

	public void setDateOfManufacturing(LocalDateTime dateOfManufacturing) {
		this.dateOfManufacturing = dateOfManufacturing;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public int compareCars(Car car1, Car car2) {
		return car1.compareTo(car2);
	}

}
