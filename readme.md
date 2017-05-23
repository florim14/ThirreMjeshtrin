![Im1](images/Im1)

Norwegian University of Science and Technology (NTNU)ThirreMjeshtrin

IMT3672 Mobile Development Practice

KOSOVARS

Project Work

November - December 2016

Mariusz Nowostawski

https://github.com/florim14/ThirreMjeshtrin.git

Weekly tasks: https://docs.google.com/spreadsheets/d/1Paz-UezNhM6qfkp4MoK_c17lucJSGpycpnv15FcFQSk/edit?usp=sharing

Much of the code taken as a reference from: https://developers.android.com

![Im2](images/Im2)

Contents

1 Introduction

1.1 Brief application description . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

2 Application core

2.1 Application architecture . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

2.1.1 Tools used . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

2.1.2 General Client-server discussion . . . . . . . . . . . . . . . . . . . . . . . . . .

2.2 Basic activity layouts and workﬂow . . . . . . . . . . . . . . . . . . . . . . . . . . . .

3 Conclusion, Issues, Diﬃculties, and what comes next?

INTRODUCTIONIntroduction

1.1 Brief application description

Our idea was based on a personal experience where the oven stopped working one day and wehad to get through the struggle of googling for possible reasons why the oven didn’t work anymore,ﬁnding and calling a repairman in the area of Gjovik through Norwegian websites and then toﬁnally setting an appointment with said repairman. After which event we thought that all of thisinformation and work could be done in one place to make our and the repairman’s life easier.ThirreMjeshtrin (translated from Albanian Call a repairman) is a complex Android and ServerSide application which does exactly what it says. It allows the user to both register as a user andreceive services from other users who are registred as repairman in their area, and to register as arepairman and receive request on behalf of other users who require their services in their own setworking radius. The application also makes it possible to instantly call a repairman, ask a repairmanfor advice through the aplication online chat, discover repairman in their surrounding, rate thoserepairman and give them feedback after having received their work.

2 APPLICATION CORE2 Application core

2.1 Application architecture

2.1.1 Tools used

Well, because it is Android, it is more than understandable that we’ve user Android Studioall the time, and of course some other technologies to implement the client-server communicationarchitecture.

On the server side, we did use php with MySQL, for processing the requests and gettingresponses from the server. The server database is in MySQL, and the main reason behind thischoice is that, we all had a course on Web Application Programming at our home university, whichwas mainly about php and MySQL working together for bulding web apps.

We’ve also used Google Firebase real-time database for implementing the chat aspect of ourproject, and we did this mainly because, Firebase itself is an ideal platform for building and inte-grating chats in Mobile apps.

(b) php

(a) Android Studio

(c) MySQL

Figure 1: Technologies used

(d) Google Firebase

![Im4](images/Im4)

![Im3](images/Im3)

![Im5](images/Im5)

![Im6](images/Im6)

2.1 Application architecture

2 APPLICATION CORE2.1.2 General Client-server discussion

Communication with the server is essential to the work of the app, considering that users canrequest immediate service from active repairmen.

The server hosts the mySQL database needed for data storage (user list, repairmen informa-tion, request registry, feedback history) and alerts single users when a change has happened onthe database which they should be notiﬁed on. This service is realised by the use of downstreammessaging (Server to Users) of Firebase Cloud Messaging. When a change happens in the databasethrough php, the server generates http requests to the aforementioned Firebase service to send amessage (push notiﬁcation) to a user identiﬁed by a token. Tokens are unique for devices, thereforea user cannot be authenticated on a device while still being logged in on another. This is easilyFigure 2: Client-server architecture in mobile devices

checked by the value of the Token for each user, since when the user is not active in any device,the Token is null. These push notiﬁcations sent by Firebase Cloud Messaging are received by theMyFirebaseMEssagingService on the device, from where notiﬁcations are built based on the contentsof the message, and needed changes are done in the app. Besides handling authentication, the serveralso notiﬁes in the event when a service is required for a certain repairman, a service request hasbeen accepted or refused, or when it has timed out.

The app contacts the server to realise events of:

 User registration and authentication

 Querying for repairmen of a chosen category in the area

 Sending requests to repairmen and handling the state of those requests

 Keeping track of the chat rooms that have been opened and chat logs (handled by the remoterealtime Firebase database)

 Saving Feedback history on accepted service requests.

Contact to the server is realised by the ConnectToServer class, which contains:

 Variables of all the needed URLs for communication

![Im7](images/Im7)

2.2 Basic activity layouts and workﬂow

2 APPLICATION CORE sendRequest method that takes a URL, parameters and a boolean indicator of whether theNetwork Connection can be asynchronously executed (in the background) or a result of thiscommunication is needed.

 results List of HashMaps of Strings which contains the parsed response from the server.The server always sends formatted responses in JSON, which are parsed and the extractedinformation is put in the results variable of the ConnectToServer object. If the connection is requiredto be synchronous, the UI thread waits until a response is returned as a result, otherwise thenetwork communication is only initiated in the UI thread, while the actual execution happens inthe background. The network communication is done in a NetworkTask object. NetworkTask is asubclass of AsyncTask where the response is also parsed from JSON.

Before a request is made to the server, the connectivity state of the device is checked, so thatthe user is notiﬁed if a network communication cannot be realised.

2.2 Basic activity layouts and workﬂow

Our application consist of several well connected activities, each of them for a diﬀerent purpose.When a user opens the app for the ﬁrst time, the user will be sent at the login activity, where theyare asked to set the login credentials, of course, if there exist a user account for them. If not, downbelow there is a clickable piece of text, and, if the user clicks on that, they would be sent at theRegistration activity.

Figure 3: Login activity

![Im8](images/Im8)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREIf we suppose that the user enters our app for the ﬁrst time, they’d need to be registered, andthe Registration activity looks like this:

Figure 4: Register activity

Of course, if they have an account, and they sent themselves in this activity, there is a way toget back to the Login activity by clicking the Already registered? Login now!.

This activity consists of two buttons, and each of them is used depending on how you’d want toregister in the app, as a Repairman (the Register as a Repairman button) or as a User (Registeras a User button).

![Im9](images/Im9)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREIf a user wants to register in the app as a Repairman, they’d be asked to give their info as shownin the activity:

Figure 5: Register as a Repairman

Each of information given in those boxes will be validated, and if not met the requirements, theuser will see an error right at the box where the info could not be validated.

The City value represents the city where the user would operate from. At the moment, thealternatives on the spinner are only the cities in Norway, and a another alternative is to get theircurrent location while registering.

The Radius value simply determines the circle area where they’re able to operate.

The Category chosen, is related to what kind of Repairman is the user. There alternativesthere are a Plumber, Electrician, and a Mechanic.

![Im10](images/Im10)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREThe registration activity for a simple user is much more simpler, requiring only the username,the email, and the password.

Here is what it looks like:

Figure 6: Register as a simple user

After clicking the Register button, the user will be created and they will be sent right at theLogin activity to login themselves for the ﬁrst time!

![Im11](images/Im11)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREAfter logging in successfully, the user would be sent to an activity where they could choose forwhat kind of Repairman they’re searching, and this by clicking on the clickable image views, whereeach of them represents a diﬀerent category of Repairman.

Figure 7: Search for a repairman

Down there is a bottom bar, helping the user to go to their proﬁle and inbox.

![Im12](images/Im12)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREImmediately after clicking on of the imageviews, a user will be sent at a map, telling their locationand some other markers (with diﬀerent color) representing the locations of the repairmans of thatcategory with the operation area including the users location where the search comes from. Notethat, if the repairman is not online, meaning, logged in the app, there won’t be a marker tellingtheir location, even if they are able to handle a service.

Those markers are clickable, so after clicking on them, the repairman proﬁle would open.Figure 8: The map view

![Im13](images/Im13)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREThis is what a repairman proﬁle would look like.

This layout is organized with tabs, so that the ﬁrst tab presents the repairmans proﬁle, thesecond tab is the Reviews tab, where you can ﬁnd the rating from diﬀerent users on that repairman,and the third tab is the Send Feedback tab, where you can send a feedback to the repairman, butonly if there is at least one deal between the user and the repairman, in which deal the user can givefeedback.

(a) Repairman proﬁle 1

(b) Repairman proﬁle 2

Figure 9: Repairman proﬁle

If there are no accepted requests between the repairman and the user, the Send Feedback tab isshowing no data. This is absolutely reasonable, since a simple user cannot rate a repairman withouthaving an accepted request from the repairman.

To get from on tab to another, you have to click the tab name, so, there is no swipe option forthis.

If the user goes at the Send Feedback tab, there will be a list of the accepted requests betweenthat user and the repairman, meaning that, the user is able to give feedback to the repairman whenclicking on one of those list items, signed with the timestamp when the request was accepted.![Im14](images/Im14)

![Im15](images/Im15)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREAfter clicking on one of the items, the Feedback activity will open, where the user can writea feedback comment, a rating via a rating bar, and all these data after being processed and sentsuccessfully will be shown in the Reviews tab of the Repairman proﬁle and all this would looksomewhat like:

(a) Repairman feedback tab

(b) Giving a feedback to a RepairmanFigure 10: Repairman feedback tab

![Im16](images/Im16)

![Im17](images/Im17)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREA Send requests button is there if the user wants to send a request to the chosen repairman,and from there will be a push notiﬁcation in the repairmans side app, notifying them that there isa request from a user for a service.

(a) Repairman getting a push notiﬁcation

(b) Repairman request prompt

Figure 11: Repairman side while getting a request for a service

The repairman can accept or deny the request. If the repairman accepts the request, there willalso be a push notiﬁcation sent at the user from where the request came from, telling him that therepairman has accepted the request. Now, if the user clicks in the push notiﬁcation they’ll be sent atthe repairmans proﬁle, with the opportunity to chat with the repairman. Deﬁnitely, a user cannotchat with a repairman without having an accepted request from that repairman.

![Im18](images/Im18)

![Im19](images/Im19)

2.2 Basic activity layouts and workﬂow

2 APPLICATION CORE(a) Client getting a push notiﬁcation

(b) The repairman proﬁle with the chat option enabledFigure 12: Client side while getting a positive request for a service

As we can see, from the client side, immediately after a positive request conﬁrmation, the usercan start chatting with the repairman.

![Im20](images/Im20)

![Im21](images/Im21)

2.2 Basic activity layouts and workﬂow

2 APPLICATION CORENow back at the bottom bar, being shown in almost each activity, with the proﬁle and the inboxoptions to select. As shown in the ﬁgure, the proﬁle button in the bottom bar sends the user to theirproﬁle (so does to the repairman too), and the inbox button will just open the inbox of the userwith the chatroooms (name of the repairman) that this particular user has opened with diﬀerentrepairmans. The same chatroom is used for all requests between a particular user and a repairman.(a) Client proﬁle view

(b) Users inbox

Figure 13: Bottom bar functionality from the Client side

![Im22](images/Im22)

![Im23](images/Im23)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREFrom the repairmans aspect of view, things seem to be a bit diﬀerent, since if a repairman clickson the proﬁle button in the bottom bar, their proﬁle is being opened with the proﬁle tab, and thereview tab.

NOTE! The send feedback tab here will be missing because, as simple as it is, a repairmancannot send feedback to themselves.

(a) Repairman proﬁle view

(b) Repairman review

Figure 14: Bottom bar functionality from the Repairman side

![Im24](images/Im24)

![Im25](images/Im25)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREAnd the Inbox layout will just list all the roomchats available for that particular repairman, so,there are listed all the clients that created chatrooms and whose requests have been accepted by therepairman.

Figure 15: Repairman inbox

![Im26](images/Im26)

2.2 Basic activity layouts and workﬂow

2 APPLICATION COREIf the user or the repairman clicks on one of those chatrooms, the chat activity will be opened,linking that speciﬁc user with the repairman. Same thing is generated, if a user, after accepting apositive reply from a repairman, clicks on the chat button in the repairmans proﬁle.

Figure 16: Inside chatroom

![Im27](images/Im27)

3 CONCLUSION, ISSUES, DIFFICULTIES, AND WHAT COMES NEXT?3 Conclusion, Issues, Diﬃculties, and what comes next?After having worked for the ﬁrst time in an Android project of this size we’re very proud of ourproject and what we achieved with it. Coordinated group work has lead to the actual implementationbeing quite close to the initial idea for the app. The challenged presented by this project during ourwork have deﬁnitly increased our skills in Mobile Application Development.

Having to work for the ﬁrst time with a real time database and implemting an application sidedonline chat, our approach for this solution was a little diﬀucult. Based on our research we deciced tobase our solution on the Firebase-powered backend because we wanted to focus on coding in Androidand less on the server side of things, which ﬁrebase allowed us to do12. The only problem was thatour backend was already implemented in a SQL Server solution for authentication, feedback registryand general user information, which lead to our application connecting, authenticating and talkingto two diﬀerent databases. We considering migrating one of the databases to the other but decicedwe didn’t have enough time to make such architectural changes in our system. We ended up usingﬁrebase for online chat and notiﬁcation purposes and MySQL for the others.

Having to work with fragments made the layouts more dynamic but understanding how theywork was more time consuming than we thought since we wanted our application to run on tabletsas well. Fragments do not behave as static layouts, which is a gift and a curse for the developersthat work with them.

Another challenged was providing the user with just the right amount of data in order for theapp to complete its main task and not ﬂood the user with unneccasary information.

Furthermore, making the application ﬂexible to provide diﬀenrent functionaly and GUI for ourtwo groups of users, regular users and repairman, required going deep into applications work ﬂowand understanding what content needs to be shown at what time.

Android has deﬁnitly made sure to make work more diﬃcult for developers with their newupdated runtime permissions. As our application requires two dangerous permissions we had tomake sure that in every part of the code where access to sensitive content is needed, the requiredpermissions are present.

Since this is only a proto-app, and of course a university project, it is more than understandablethat it can be extended on its functionality. Our application oﬀers only a few Repairman category,and of course, adding more Repairman categories would make the app more complete, and moreattractive to the users.

We might also think on changing the way data ﬂow from the client to the server and backwards,because as there are more data, there is a risk of a overload for the server and the connectiongenerally.

We’ve manipulated with two diﬀerent databases, and, another improvement would be a singledatabase for the application.

1http://myapptemplates.com/simple-android-chat-app-tutorial-firebase-integration/

2http://softwareengineering.stackexchange.com/a/262504

