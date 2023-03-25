package uk.gov.dwp.uc.pairtest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

	@Autowired
	TicketPaymentService ticketPaymentService;
	
	@Autowired
	SeatReservationService seatReservationService;
    
	@Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

		
		try {
			
			int childsum=0;
			int infantsum=0;
			int adultsum=0;
			int ammount=0;
			
			List<TicketTypeRequest> ticketTypeRequestlist= new ArrayList<>();
			if(accountId>0) {
				
				for(TicketTypeRequest req:ticketTypeRequests) {
					
					ticketTypeRequestlist.add(req);
				
				
				for(TicketTypeRequest list:ticketTypeRequestlist) {
					
					if(list.getTicketType()==Type.CHILD) {
						
						childsum+=list.getNoOfTickets();
					}
					
					else if(list.getTicketType()==Type.INFANT) {
						
						infantsum+=list.getNoOfTickets();
					}
					else {
						adultsum+=list.getNoOfTickets();
					}
				}
				
				if(adultsum>=childsum+infantsum) {
					
					ammount=(adultsum*20)+(childsum*10);
					ticketPaymentService.makePayment(req.getNoOfTickets(), ammount);
					seatReservationService.reserveSeat(accountId, adultsum+childsum);
				}
			}
			}
		}catch(InvalidPurchaseException e) {
			
			System.out.println("Exception occured while Purchasing the tickets::"+e);
		}
    }

}
