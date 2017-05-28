# Compus-Mobile-Application

+ Introduction
+ Welcome Activity
+ Main Activity
+ Login Activity
+ Weather/Announcement/News Activity
+ APK Installation
---
## Introduction
This is the graduation project for my bachelor degree.I finished it in August 2015, and I didn't have a GitHub account at that time, I find it today on my old laptop and decide to upload.Basically, this is my first project with Android, I start from this.Concretely, this is a complex of many functionalities.You can see the map here, you can log in and check your grade here(I can't check my grades due to the cancellation of my campus account), you can receive announcements from the administrator...

Now,I will introduct this app and all of its functionalits by each window in order.


## Welcome Activity
---
![Welcome Activity](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/welcome.png)
---
Basically,I design this Activity for show the logo of my mother school and get some time to prepare for next activity.After 5 seconds,when the next activity(main activity) is ready,this activity will disapear.

## Main Activity
---
![Main Activity](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/main.png)

---
This is the main activity of this application.You only can start launch all the functions form here.You can use and check the map here,you can see some latest news from the Marquee bar(on the bottom of this activity,it will update form the university website automatically),it will send show you the latest announcement by a popup(announcement is get from a specific blog I set,the app will grasp the data from that blog automatically,when it finds an information new,it will show you that by a popup).

### About MAP
---
In this part,I use the BAIDU MAP API to achive my function,this is not very complicated,I refered to the official tutotials online.

### About Popup
---

A thread will check the blog per 2 seconds,once it fins any new announcement,it will tell the handler to show you a popup with the new announcement.


![Main Activity](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/information.png)



### About Marquee Bar
---
when this app was launced,our app will check the NJUPT(Nanjing University of Posts and Telecommunications) website and downloads three latest news and show it on the Marquee bar，it will show the motto of NJUPY as well.

## Login Activity
---
This is the hardest part I faced on this entire project.In This part,I have to mocking logging in the grade system and get the grades back and dis play them,I need to use much html knowledge.At first of all,I download the Security code picture and display it on the activity,after user enter user name,password and Security code,I will use this information to mock loging in the system.Since I didn't know and I can't anything useful about how the grade system work,I have to test it again and again myself,I still remeber how much time it costed me to finish this part.After achiveing this function,user and check his grades in this app directly.

---

![Login Activity](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/login.png)

---

![Grade](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/grade.png)


## Weather/Announcement/News Activity
---

This part is very easy,when user click these parts,the app will open a new activity and redirect the new activity to a specific link.It's like a browser here.

---
![Weather](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/weather.png)
---
![Announcement](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/announcement.png)
---
![News](https://raw.githubusercontent.com/s2117402/Compus-Mobile-Application/master/Image/news.png)
---

## APK Installation

[Download APK](https://github.com/s2117402/Compus-Mobile-Application/tree/master/APK)
