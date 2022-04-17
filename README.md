# COMP4200-Group22-Project


Please note that much of the code, such as front end code, was peer programmed so other names may not appear as often on the commit history.

Work done by each group member:

### Rutu

Rutu implemented the entirety of the back end. He created all PHP scripts to receive and process 
requests, and also provided an API with documentation for use on the front end. He implemented 
a java class and helper methods to streamline making and managing requests, as well as the 
necessary structures to allow Volley to be used by application. This mainly refers to the 
RequestHandler singleton. He also made a global activity to generalize certain functionalities for 
all activities, such as adding http requests to the request handler singleton. Outside of the 
backend development, Rutu helped with the process of linking front end functionality to backend 
API calls, and general bug fixes. And lastly, Rutu did the back-end portions of report and 
presentation.

### James
Once the design layouts were created, James added the functionality to the front end of the 
application. This involved adding button click handlers, setting up edit texts, implementing the 
navigation bar, implementing theme selection, etc. Additionally, James dealt with all the local 
data that was stored in shared preferences. This included the user credentials if the user chose to 
stay logged in, and the currently selected colour theme. Once the database and backend were 
completed, James also implemented all the calls from the application to the database to ensure 
that the correct data was pulled from the database, depending on parameters such as the current 
user, and the currently selected vehicle. In summary, James worked in the middle of the 
frontend, and the backend to ensure they could work together and make the application 
functional. Lastly, James put together the demo video from the presentation, wrote the user 
manual, and the front-end portion of this report.

### Ann

Ann came up with the design ideas for the various different activities of the application. The 
main screen was designed to allow the user to display all the purchases made for a specific 
vehicle. The user can see how much is spent on gas, maintenance, insurance, as well as other 
miscellaneous purchases for the selected vehicle. The purchases page was designed to show aa 
list of the purchases made for the specified vehicle. The Vehicle list screen was designed to 
allow the user to add or take away vehicles from they list. Once the ideas for the designs were 
created, Ann implemented the XML layouts for the activities. Later in the development process, 
Ann implemented more layouts for things like alert dialogs, and list adapters.

### Jaime

Jaime developed the database table schema that would be implemented to hold our data. He 
designed 4 four different tables that would be used. The "User" table is designed to hold user 
credentials such as username and password. The Vehicle table holds vehicle names, and each 
entry corresponds to a specific user. The Purchase table holds a list of purchases that are all 
assigned to a specific user, and a specific vehicle. Finally, the stat table is designed to hold 
certain stats that will be assigned to a specified user and vehicle.
