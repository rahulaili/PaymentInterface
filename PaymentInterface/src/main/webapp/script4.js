/**
 * 
 */
/**
 * 
 */

const payment = () => {
    console.log("payment started");
    var amount = $("#paymentfield").val()
    console.log(amount);
    if (amount == "" | amount == null) {
        alert("amount is required")

        return
    }

    //ajax code
    //we are using ajax to send request to server jquery ajax




    $.ajax({

        url: "create_order",
        data: JSON.stringify({ amount: amount, info: 'order_request' }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            console.log(response)
            if (response.status == "created") {
                var options = {
                    "key": "USE YOUR APPI KEY", // PLEASE USE YOUR OWN API KEY OF RAZORPAY
                    "amount": response.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
                    "currency": "INR",
                    "name": "Donation Center", //your business name
                    "description": "Happiness does not result from what we get, but from what we give",
                    "image": "https://img.freepik.com/free-vector/people-carrying-donation-charity-related-icons_53876-43091.jpg",
                    "order_id": response.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
                    "handler": function (response) {
                        

                        updatepaymentonserver(response.razorpay_payment_id,response.razorpay_order_id, "paid");
                        swal({
				              title: "Donation Successful",
				              text: "Thank you for your Donation Helps a lot",
				              icon: "success",
				              button: "Done",
            			});
                    },
                    "prefill": {
                        "name": "", //your customer's name
                        "email": "",
                        "contact": ""
                    },
                    "notes": {
                        "address": "Razorpay Corporate Office"
                    },
                    "theme": {
                        "color": "#4295f5"
                    }
                };
                var rzp = new Razorpay(options);
                rzp.on('payment.failed', function (response) {
					swal({
				              title: "Oops! Transaction Cancelled/Failed",
				              text: "Please Try agiain",
				              icon: "error",
				              button: "Done",
            			});
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                });
                rzp.open();

            }

        },
        error: function (response) {
            console.error("something went wrong");
        }

    })
};

//update paymment on server

function updatepaymentonserver(payment_id,order_id,status) {
    $.ajax({
        url:"update_order",
        data: JSON.stringify({
            payment_id: payment_id,
            order_id: order_id,
            status: status,
        }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            
        },
        error: function (error) {
            
        },
    })
}/**
 * 
 */
