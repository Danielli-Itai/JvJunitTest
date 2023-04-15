package pckg_telephony;



public class TelephoneMain
{
	public static void main(String[] args)
	{
		Telephone phone1 = new Telephone("123456","Almo");
		Telephone phone2 = new Telephone("888888","Ogi");
		
		TelephoneCall call1 = new TelephoneCall();
		if(call1.MakeCall(phone1, phone2)) {
			System.out.println(call1.ToStrin());
		}
		return;		
	}
}
