package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.constants.ApplicationConstants;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
	/**
	 * Should only have private methods other than the one below.
	 */

	private int totalNoOfTickets = 0;
	private int noOfAdultTickets = 0;
	private int noOfChildTickets = 0;
	private int noOfInfantTickets = 0;
	private int amountToBePaid = 0;

	@Override
	public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) {
		try {
			if (isValidAccountId(accountId)) {
				for (TicketTypeRequest ticketRequest : ticketTypeRequests) {
					validateTicketPurchase(accountId, ticketRequest);
				}
				processTicketRequest(accountId, totalNoOfTickets, noOfAdultTickets, noOfChildTickets,
						noOfInfantTickets);
			}
		} catch (InvalidPurchaseException e) {
			System.err.print("Exception Occurred while processing your request," + e.getMessage());
		}

	}

	private void processTicketRequest(Long accountId, int totalNoOfTickets, int noOfAdultTickets, int noOfChildTickets,
			int noOfInfantTickets) {
		System.out.println("********* No of Tickets ************* \n" + totalNoOfTickets);
		System.out.println("********* Adult Tickets ************* \n" + noOfAdultTickets);
		System.out.println("********* Child Tickets ************* \n" + noOfChildTickets);
		System.out.println("********* Infant Tickets ************* \n" + noOfInfantTickets);
		System.out.println("********* Amount To be Paid ********* \n" + amountToBePaid);
		boolean isPaymentDone = processPayment(accountId, amountToBePaid);
		if (isPaymentDone) {
			System.out.println("********* Payment Success! ********* \n" + amountToBePaid);
			reserveSeats(accountId, totalNoOfTickets);

		}

	}

	private void validateTicketPurchase(Long accountId, TicketTypeRequest ticketRequest) {

		if (ticketRequest.getTicketType() == Type.ADULT) {
			noOfAdultTickets = ticketRequest.getNoOfTickets();
		} else if (ticketRequest.getTicketType() == Type.CHILD) {
			noOfChildTickets = validateChildTicketPurchase(ticketRequest, noOfAdultTickets);
		} else if (ticketRequest.getTicketType() == Type.INFANT) {
			noOfInfantTickets = validateInfantTicketPurchase(ticketRequest, noOfAdultTickets);

		} else {
			throw new InvalidPurchaseException(" Ticket Request Type is Invalid!. ");
		}
		totalNoOfTickets = noOfAdultTickets + noOfChildTickets + noOfInfantTickets;
		if (isValidNoOfTickets(totalNoOfTickets)) {
			amountToBePaid = culculateAmountTobePaid(noOfAdultTickets, noOfChildTickets);
		} else {
			throw new InvalidPurchaseException(" Only a maximum of 20 tickets that can be purchased at a time ");
		}
	}

	private void reserveSeats(Long accountId, int totalNoOfTickets) {
		SeatReservationService reservationService = new SeatReservationServiceImpl();
		reservationService.reserveSeat(accountId, totalNoOfTickets);
		System.out.println("\n********* Reservation Done,have a great watching! ************* \t\n\n");

	}

	private boolean processPayment(Long accountId, int amountToBePaid) {
		System.out.println(
				"\nNote:\nInfants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap.\n ");
		TicketPaymentService paymentService = new TicketPaymentServiceImpl();
		paymentService.makePayment(accountId, amountToBePaid);
		return true;

	}

	private int culculateAmountTobePaid(int noOfAdultTickets, int noOfChildTickets) {
		return (noOfAdultTickets * ApplicationConstants.ADULT_TICKET_PRICE)
				+ (noOfChildTickets * ApplicationConstants.CHILD_TICKET_PRICE);
	}

	private int validateInfantTicketPurchase(TicketTypeRequest ticketRequest, int noOfAdultTickets) {
		int noOfInfantTickets = 0;
		if (ApplicationConstants.isAdultTicketExist(noOfAdultTickets)) {
			noOfInfantTickets = ticketRequest.getNoOfTickets();
			return noOfInfantTickets;
		} else {
			throw new InvalidPurchaseException(
					" Child and Infant tickets cannot be purchased without purchasing an Adult ticket. ");
		}

	}

	private int validateChildTicketPurchase(TicketTypeRequest ticketRequest, int noOfAdultTickets) {
		int noOfChildTickets = 0;
		if (ApplicationConstants.isAdultTicketExist(noOfAdultTickets)) {
			noOfChildTickets = ticketRequest.getNoOfTickets();
			return noOfChildTickets;
		} else {
			throw new InvalidPurchaseException(
					" Child and Infant tickets cannot be purchased without purchasing an Adult ticket. ");
		}

	}

	private boolean isValidNoOfTickets(int noOfTickets) {
		return noOfTickets <= ApplicationConstants.MAX_TICKET_ALLOWED ? ApplicationConstants.VALID_PURCHASE
				: ApplicationConstants.INVALID_PURCHASE;

	}

	private boolean isValidAccountId(Long accountId) {
		if (accountId > 0) {
			return ApplicationConstants.VALID_PURCHASE;
		} else {
			throw new InvalidPurchaseException(" Invalid Purchase Request : Unauthorized Access,Invalid Account Id! ");
		}
	}

}
