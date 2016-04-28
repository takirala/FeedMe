# Smart Food Cluster

Mobile Computing (CNT 5517) class project

Our approach is to bring a smart food delivery system which provides the convenience and flexibility to both user and the restaurant/food truck. Users can order the food in advance by our app, and it will generate a QR code. When the restaurants receive the order, they can start cooking before the busy lunch/dinner time. As long as the food is ready, they can deliver them to our smart food clusters placed at strategic locations. Use the QR code associated with the order to open the smart food storage box. The food can be placed in the box safely, freshly, and warmly if it needs. After that, user can pick up the food whenever he/she wants using the QR code to open the box smart food storage box. This allows small restaurants and food trucks the infrastructure to take and service orders online

## Data Flow
The user registration - ordering - payment - pick up flow can be seen in more detail below :

![User Flow](/docs/userFlow.jpg)

Similary, the restaurant/foodtruck owner flow can be seen in more detail below:

![Restaurant Flow](/docs/resflow.jpg)

## Modules

Smart Food Cluster has three crucial modules that act as the backbone of its architecture. User side android application, Google App engine backend & of course the Brillo module to communicate with Weave and Intel Edison board.

### User side android application

The android based application has various functionalities that enable the user to :
- The mobile app represents the application that would enable the users to place orders for food. 
- App uses Google places api which uses the best location available (GPS, fallback to Internet, fallback to last available location) to query the nearby places and provides the list of restaurants
- App enables the user to make an order. User payment is simulated using a dummy app engine url. After the payment is succeded the user is provided with the QR Code. 
- The QR Code is stored in the user application and can be referred for scanning at the food cluster in the future. 
- QR Code generated using libaries at [Zxing](https://github.com/zxing/zxing)

The same can be seen in below screenshots of the application.

| User Login | Restaurant Search | Payment Success |
| --- | --- | --- |
| ![User Login](/docs/login.jpg)  | ![Restaurant Search](/docs/resSearch.jpg) | ![Payment Success](/docs/paymentSuccess.jpg) |

### Backend

 - Created the backend using Google cloud platform. 
 - The application was hosted on google App engine. 
 - Database was created on Google cloud datastore. 
 - Data was accessed from app and stored back using Objectify as a data access API. 
 - Interaction with App engine was designed using Google cloud End points. 

### Sensor platform

 - We are using Brillo core services for connecting our applications out-of-the-box. 
 - Weave enabled device to cloud communication enables our user to interact with food clusters through mobile applications. 
 - We built a robust web service by using “Google Weave API” which provides REST API integrations. 
 - Through these REST APIs we are inspecting and sending commands to online weave device for execution. 
 - For experimenting the sensor module of our system we tried to program a light sensor (TSL 2561) and a moisture sensor. 
 - Light sensor, when the box is open, senses the light. Brillo now turns ON the led until the box is closed. 
 - A moisture sensor can sense the moisture level in the box. 
 - By using these readings, we can also build a feedback mechanism and let the end users know the status. 

Digital Luminosity/Lux/Light Sensor integration along with the breakout board & blinking of LED upon validating the QRCode be seen the image below:

![Edison Board](/docs/edison.jpg)
 
Write to us for any feedback or queries. This project is currently not under development.
