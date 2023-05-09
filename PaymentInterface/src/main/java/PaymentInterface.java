
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONObject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.google.gson.Gson;
import com.razorpay.Order;

/**
 * Servlet implementation class PaymentInterface
 */
@WebServlet("/create_order")
public class PaymentInterface extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public PaymentInterface() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Set up the RazorpayClient object with your API key and secret key
		
		
		
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String json = sb.toString();
		Gson gson = new Gson();
	      PaymentData paymentData = gson.fromJson(json, PaymentData.class);

	      // Get the amount value from the PaymentData object
	      int amount = (int) paymentData.getAmount();
	      System.out.println(amount);
		
		// Parse the JSON data and do something with it


		RazorpayClient razorpayClient;

		try {
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100);
			orderRequest.put("currency", "INR");
			Order order;
			//YOU HAVE TO GENERATE YOUR API KEY IN RAZORPAY
			razorpayClient = new RazorpayClient("PLEASE ENTER YOUR RAZORPAY API KEY HERE", "PLEASE USE YOUR SECRET SCODE HERE");
			
			order = razorpayClient.Orders.create(orderRequest);
			System.out.println(order);
			
			String orderJson = order.toString();
			
		    

		    // Set the response content type to application/json
			
		    response.setContentType("application/json");
		    response.getWriter().write(orderJson);
			
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demopayment?allowPublicKeyRetrieval=true&useSSL=false","root","6761");
			PreparedStatement pst = con.prepareStatement("insert into orders(orderId,amount,recipt,statuss) values(?,?,?,?)");
			pst.setString(1,order.get("id"));
			pst.setString(2, order.get("amount").toString());
			pst.setString(3, order.get("recipt"));
			pst.setString(4, order.get("status"));
			pst.executeUpdate();
			
			con.close();
			
			
			

		} catch (RazorpayException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		
		}
		
		

	}

}

class PaymentData {
	  private double amount;

	  public double getAmount() {
	    return amount;
	  }

	  public void setAmount(double amount) {
	    this.amount = amount;
	  }
	}
