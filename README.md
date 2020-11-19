# oop
 G1T4's Object Oriented Programming Project

**Project Functional Requirements**

For the scope of this project, you are required to develop a Java application that uses PORTNET&#39;s Application Programming Interface (API) to obtain real-time information about vessel berthing times. This application will be used by forwarders to plan the schedule for truckers.

The project will require you to build a client-server application. This application will be used by forwarders with a high number of internal users. The goal is to reduce the number of hits on PSA web service (see Appendix A).

**Server Application**

1. The server application calls the web service (API) using an access key on a regular interval to get the vessels&#39; arrival timings and store them in a relational database. The following values should be configurable without the need to re-compile code:
  1. Time interval to call the API
  2. Web Service Access key

1. By default, the web service is called at 00:00 hours daily to retrieve the vessels&#39; arrival timings for the next 7 days (excluding the current day).

1. The web service is also called hourly to retrieve the vessels&#39; arrival timing for the current day (00:00 - 23:59).

**Client Application**

1. The client application could be a Java web-frontend or a Java Graphical User Interface(GUI).

1. The client application should allow users with certain emails to register (e.g. @sis.smu.edu.sg). This should be configurable without the need to recompile code so that PSA can distribute the application to its PORTNET users (forwarders).

1. After registration, a user is allowed to login to the client application with the registered email ID and password. There should be an option for registered users to reset their passwords.

1. After logging in, the user sees the vessel schedule for the current day. He can choose another day (up to 7 days in advance). If today is Monday, the latest daily schedule that he can view is next Monday. The fields displayed must include:
  1. vessel&#39;s name
  2. incoming voyage number
  3. outgoing voyage number
  4. Berthing Time
  5. Departure time
  6. Berth Number
  7. Status
  8. Change count (i.e. Number of times the berthing time changed). This is tracked by your server application.
  9. The degree of change in vessels&#39; arrival timing based on the first and the latest timings pulled by your server application (marked Yellow for less than an hour change, Red for 1 hour or more)

The resultant output must be allowed to be sorted by a specific field.

1. The vessel&#39;s name and incoming/outgoing voyage number (Point 4, a-b) uniquely identify a shipment. A user can track a shipment by adding the vessel&#39;s name and voyage number to his list of favourites.

1. The user can view his list of favourites. The same output should be displayed (Point 4, a - i). Your client application&#39;s interface must allow Favourites&#39; list to be modified.

1. The application must allow the user to subscribe to be notified for changes in arrival/departure timing of a specific vessel (given a vessel&#39;s name and incoming/outgoing voyage number). An email notification is expected to be sent wherever there is a change in the arrival timing.