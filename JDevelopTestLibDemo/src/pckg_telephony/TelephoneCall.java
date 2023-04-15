package pckg_telephony;


public class TelephoneCall 
{
	private String caller = null;
	private String called = null;
	
	public boolean MakeCall(Telephone from, Telephone to) {
		boolean result = true;
		
		if((!from.getOccupied())&&(!to.getOccupied())) {
			this.caller = from.ToString();
			this.called = to.ToString();
		}
		return result;
	}
	public String ToStrin() {
		return this.caller + " called " + called;
	}
}
