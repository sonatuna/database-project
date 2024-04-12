package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single branch
 */
// this code is coped from demo project for setup reason
// make sure everyone in the group have same setup right now
public class BranchModel {
	private final String address;
	private final String city;
	private final int id;
	private final String name;	
	private final int phoneNumber;
	
	public BranchModel(String address, String city, int id, String name, int phoneNumber) {
		this.address = address;
		this.city = city;
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}
}
