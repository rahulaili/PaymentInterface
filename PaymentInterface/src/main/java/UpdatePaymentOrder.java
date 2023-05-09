

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdatePaymentOrder
 */
@WebServlet("/update_order")
public class UpdatePaymentOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UpdatePaymentOrder() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String json = sb.toString();
		Gson gson = new Gson();
	    PaymentDatas paymentOrderData = gson.fromJson(json, PaymentDatas.class);

	      // Get the amount value from the PaymentDatas object
	      String paymentid = paymentOrderData.getpaymentid();
	      System.out.println(paymentid);
	      
	      String orderid = paymentOrderData.getorderid();
	      System.out.println(orderid);
	      
	      String status = paymentOrderData.getstatus();
	      System.out.println(status);
	      
	      response.setContentType("application/json");
		  
	      
	      try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Accounts?allowPublicKeyRetrieval=true&useSSL=false","root","6761");
			PreparedStatement pst = con.prepareStatement("update orders set productstatus=? where orderid = ?");
			pst.setString(1,status);
			pst.setString(2,orderid);		
			pst.executeUpdate();
			
			con.close();
	      
	      } catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		
	}

}

class PaymentDatas {
	  private String payment_id;

	  public String getpaymentid() {
	    return payment_id;
	  }

	  public void setPaymentid(String payment_id) {
	    this.payment_id = payment_id;
	  }
	  
	  
	  private String order_id;

	  public String getorderid() {
	    return order_id;
	  }

	  public void setOrderid(String order_id) {
	    this.order_id = order_id;
	  }
	  
	  
	  private String status;

	  public String getstatus() {
	    return status;
	  }

	  public void setstatus(String status) {
	    this.status = status;
	  }
	}
