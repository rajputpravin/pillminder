# pillminder
This is data consumer app for the data generated by pillminder capture using IOT device.

The data generated by pillminder capture app that we stored on cloud mongo DB will be fetced using REST webserives and used by our consuming app called pillminder. This data will then be used to display virtual pill box on android app and alert care taker in case of any missed or over dose.

## Inspiration
During these very busy days, it is hard to keep track of the hourly medicines that elderly and people with special needs consume. There have been many incidents recently how people have suffered because of overdose or health issues because they might have missed to take a pill or two.
We wanted to develop a model that keeps track of these pills and sends alerts or reminders to their care-takers.

## What our App does?
Our PillMinder app always monitors the pillbox which contains the medicines organized for the entire week. This camera constantly monitors the pillbox and talks to the backend server which communicates to the mobile app which can be installed by the caretakers. If there is any missing dose or overdose . It immediately alerts the caretaker, and they can take necessary actions.

## The Five Ws
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/FiveWhys.PNG?raw=true)

## Basics
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/architectureDiag.PNG?raw=true)

## Screenshots
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/login.PNG?raw=true)
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/emptyPillBox.PNG?raw=true)
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/filledPillBox.PNG?raw=true)
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/WeekCalendarView.PNG?raw=true)
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/pullUpNotification.PNG?raw=true)
![alt text](https://github.com/rajputpravin/pillminder/blob/master/artifacts/sideMenuView.PNG?raw=true)

## What's next: The Smarter PillBox
- More efficient Algorithms can be used to detect and analyze the type and number of pills 

- This model can be extended to be used in different type of Pillboxes

- Further analysis of data can de done to find any patterns that will help us understand patient’s behavior



