import org.junit.Before;
import org.junit.Test;

import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketServiceTest {

	private TicketService ticketService;

	@Before
	public void setUp() {
		ticketService = new TicketServiceImpl();
	}

	@Test
	public void testPurchaseTicket() {
		TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 10);
		TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 5);
		TicketTypeRequest infantTicket = new TicketTypeRequest(Type.INFANT, 5);
		ticketService.purchaseTickets(1l, adultTicket, childTicket, infantTicket);
	}

	@Test
	public void testPurchaseAdultTicket() {
		TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 10);
		ticketService.purchaseTickets(1l, adultTicket);
	}

	@Test
	public void testPurchaseChildWithAdultTicket() {
		TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 10);
		TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 1);
		ticketService.purchaseTickets(1l, adultTicket, childTicket);
	}

	@Test
	public void testPurchaseInfantWithAdultTicket() {
		TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 10);
		TicketTypeRequest childTicket = new TicketTypeRequest(Type.INFANT, 1);
		ticketService.purchaseTickets(1l, adultTicket, childTicket);
	}

	@Test
	public void testPurchaseChildTicketWithNoAdultTickets() {
		TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 0);
		TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 5);
		ticketService.purchaseTickets(1l, adultTicket, childTicket);
	}

	@Test
	public void testPurchaseInfantTicketWithNoAdultTickets() {
		TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 0);
		TicketTypeRequest infantTicket = new TicketTypeRequest(Type.INFANT, 5);
		ticketService.purchaseTickets(1l, adultTicket, infantTicket);
	}
	@Test
	public void testPurchaseMoreThanThreshold() {
		TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 10);
		TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 5);
		TicketTypeRequest infantTicket = new TicketTypeRequest(Type.INFANT, 10);
		ticketService.purchaseTickets(1l, adultTicket,childTicket, infantTicket);
	}
}
