package uk.gov.dwp.uc.pairtest.constants;

public class ApplicationConstants {

	public static final int MAX_TICKET_ALLOWED = 20;
	public static final boolean VALID_PURCHASE = true;
	public static final boolean INVALID_PURCHASE = false;
	public static final int ADULT_TICKET_PRICE = 20;
	public static final int CHILD_TICKET_PRICE = 10;

	public static boolean isAdultTicketExist(int noOfAdultTicket) {
		return noOfAdultTicket > 0 ? VALID_PURCHASE : INVALID_PURCHASE;
	}

}
