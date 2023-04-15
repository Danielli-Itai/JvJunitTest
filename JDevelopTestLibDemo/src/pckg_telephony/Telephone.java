package pckg_telephony;



public class Telephone 
{
	private String line_number = "";
	public String getNumber() {
		return this.line_number;
	}
	
	private String line_owner = "";
	public String getOwner() {
		return this.line_owner;
	}

	private boolean occupied = false;
	public boolean getOccupied() {
		return occupied;
	}
	public boolean setOccupied(boolean state) {
		return occupied = state;
	}

	Telephone(String number,String name){
		line_number = number;
		line_owner = name;
		return;
	}

	public String ToString() {
		return this.line_number + "," + this.line_owner;
	}
}
