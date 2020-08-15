package car.model;

import java.time.LocalDateTime;

public class Bike extends Vehicle implements Cloneable {

	//
	private int bikeId;
	private int gear;
	Vehicle vehicle;
	//
	
	public Bike(int gear, int bikeId, int totalVehicleInstantiated, String name, LocalDateTime dateOfManufacturing, int id) {
		super(totalVehicleInstantiated, name, dateOfManufacturing, id);
		
		this.bikeId=bikeId;
		this.gear= gear;
	}

	public int getBikeId() {
		return bikeId;
	}

	public void setBikeId(int bikeId) {
		this.bikeId = bikeId;
	}

	public int getGear() {
		return gear;
	}

	public void setGear(int gear) {
		this.gear = gear;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	@Override
	public Object clone() {
		try {
		return	super.clone();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
		
	}
}
