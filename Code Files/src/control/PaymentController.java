package control;

public class PaymentController {
	private PaymentDAO payment;
	
	public PaymentController() {
		this.payment = new PaymentDAO();
	}
	
	public void registerPayment(int reservationID, double amount, String method) {
		payment.recordPayment(reservationID, amount, method);
	}
}
