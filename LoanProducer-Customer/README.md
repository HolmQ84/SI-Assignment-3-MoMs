SYSTEM INTEGRATION - MINI PROJECT 3

Message Oriented Middleware

This application uses Kafka and RabbitMQ to send data from client to server and vise versa.

1. The application simulates a customer (LoanProducer-Customer) which send a loan request to several different banks, 
using Kafka messaging system.

2. The different banks processes the data sent by the customer, and returns a loan offer depending on their individual loan
calculators, which takes the customers information about income, debt and ownership of house/car into consideration.

3. The customer then applies for one of the loans presented to him.

4. The bank receives the loan acceptance and sends the data to a contract handler (Contract), which then returns the
contract as a pdf file for the customer to download.

----------------------------------------------------------------------------------------------------------------------

**To start the application:**

- Launch local Kafka Zookeeper and Server on default ports.
- Launch local RabbitMQ server on default port.
- Start the 5 different applications:
  - Contract
  - DanskeBankConsumerApplication
  - NordeaConsumerApplication
  - NykreditConsumerApplication
  - LoanProducer-Customer


- Launch postman and make a Post-Request for ***localhost:8081/loanrequest***
  - Remember header info (*Content-Type = application/json*)
  - Use this format in body:
  
{
    "customerName": "John Doe",
    "customerTitle": "Mr.",
    "yearlySalary": "200000",
    "debtAmount": "50000",
    "carOwner": false,
    "houseOwner": true
  }


- Make a GetRequest at ***localhost:8081/loanOffers*** for incoming loan offers.
- Select one offer, and copy the loanId.
- Make a PostRequest at ***localhost:8081/acceptLoan/{loanId}*** with the loanId.
- Go to your browser and type in ***localhost:8081/download/{loanId}***
  - Your Loan Contract should be downloadable with info about interest and payments.