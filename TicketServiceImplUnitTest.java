package uk.gov.dwp.uc.pairtest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplUnitTest {

	@InjectMocks
	TicketServiceImpl ticketServiceImpl;
	
	@Mock
	TicketPaymentService ticketPaymentService;
	
	@Mock
	SeatReservationService seatReservationService;
	
	@Before
	public void initMock() {
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void purchaseTicketsTest() {
		
		TicketTypeRequest ticketTypeRequest= new TicketTypeRequest(Type.ADULT,2);
		
		Long accountId=7l;
	
	Mockito.doNothing().when(ticketPaymentService).makePayment(Mockito.anyLong(), Mockito.anyInt());
	
	Mockito.doNothing().when(seatReservationService).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
	
	ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequest);
	
	verify(ticketPaymentService,times(1)).makePayment(Mockito.anyLong(), Mockito.anyInt());
	verify(seatReservationService,times(1)).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
	
	}
	
	@Test
	public void purchaseTicketTestException() {
		
        TicketTypeRequest ticketTypeRequest= new TicketTypeRequest(Type.CHILD,2);
		
		Long accountId=7l;
		
		Mockito.doThrow(new InvalidPurchaseException("exception")).when(ticketPaymentService).makePayment(0, 0);
		
	}
	
	
	}
