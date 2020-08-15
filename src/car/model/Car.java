package car.model;

import java.time.LocalDateTime;

public class Car extends Vehicle implements Comparable<Car> {
	// Instances variables
	private String brand;
	private double price = 0.0;
	private String color;
	private int id;

	// getter and setter methods

	public Car(int totalVehicleInstantiated, String name, LocalDateTime dateOfManufacturing, int id, String brand,
			double price, String color, int id2) {
		super(totalVehicleInstantiated, name, dateOfManufacturing, id);
		this.brand = brand;
		this.price = price;
		this.color = color;
		id = id2;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(Car otherCars) {
		if (this.price > otherCars.price) {
			return 1;
		} else {
			if (this.price < otherCars.price) {
				return -1;
			}
		}
		return 0;

	}

}
