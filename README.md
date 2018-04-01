**Name:** Yahya Almardeny.

**ID:** 20072732.

**Project Name:** WIT Selfie Competition.

**Version:** 1.0

**Date:** 16/03/2018
 
**Github Repository URL:** https://github.com/John-Almardeny/selfie_competition


 - - - -


**Introduction:**

This Selfie Competition App shall add some social value to the students’ life in WIT and make them get to know each other via having some constructive fun and good memories.


**App Structure:**

Current Overall Structure: 6 *Activities*, 4 *Fragments* and 8 *Models*.


**GUI Design:**

I followed an Agile approach with sprints of 1-week long each. I have 3 users and in every iteration I get their feedback and change the design accordingly.<br>
I concentrated on colors that make the app visually more appealing. I left an option for the user to change the Theme(colors) of the app in the *Settings*.<br>
I used in my design a *Navigation Drawer* to allow users to easily navigate to different parts of the application without having to go through a set of activities.<br>
the main Ux guidelines reference I followed is the *"User Interface Guidelines"* published on the official Android website.<br>
The design is inituitive and all the edits/changes/updates happen on a single page.<br>
When the procedure requires a `lock`, a `ProgressBar` will popup on the top of everything in order not to ruin/interrupt the current operation. When it's possible, the procedure is done asynchronously. 



**GIT Repository:**

I use Github as a VC and I created a local and remote repo for my application at the same time. After each iteration I add what I've done into the stage, commit with an appropriate message and push to the origin repo remotely.<br>
Please refer to the related Gist at: https://gist.github.com/John-Almardeny

**Git Logs** (Please refer to https://github.com/John-Almardeny/selfie_competition/pulse/monthly):

1 author has pushed 19 commits to master and 19 commits to all branches. <br>
On master, 95 files have changed and there have been 5,030 additions and 425 deletions.

**Github Repository URL:**
 
https://github.com/John-Almardeny/selfie_competition


**App Functionality**

* Splash Screen: Logo for app, asynchronous checking with database for the conditional login (first time signin, kept signedin, joined but not verified or not registered..etc).

* Register: Create a new user account and join the WIT Community (ask for valid email and password(repeated) and send verification link to the user email and inform the user on app. Temporarily I commented out the checking for `@wit.ie` suffix in the email to allow testing for now.

* SignIn: Sign in with email and password, validate and interact accordingly.

* Forgot Password: Send a reset password link to user's email if they are already registered.

* Update Password: Validate current password and update it.

* First-time Profile Setup: After the very first login, it asks for basic user information before allowing join the community as it's mandatory to know the users because they submit images.

* Main: Main Activity with a Drawer Navigation, fully interactive and intuitive.

* Profile: It shows editable user's information and changeable profile picture.

* Settings: Change the App Theme. Change and Update The Password. Close and Delete the User Account. About WIT Seflie Competition App.

* Competition: TO-DO in CA2 with additional DB Collections and Nodes.

* Gallery: TO-DO in CA2 with additional DB Collections and Nodes.

* User: A Model Class to represent User Collection in Database for JSON implementation and manipulation (Read, Update, Delete, Create).

* SharedPreferences Listener: NOTE THIS IS NOT USED ANYMORE. KEPT FOR ANY FUTURE USAGE - I developed a way to listen to the updates in values by given list of fields in a given shared preferences file then invoke a given method wrapped by Callable once all fields are updated.

* Helper: Contains Load of public static methods I developed specifically to accelerate, ease and compact the work, also to avoid loads of duplicates of code among different activities and classes (i.e. for best coding practice).

* EditTextViewListener: A workaround I developed to listen to the text changes in a given `EditText`/`TextView` and calculate the required width to keep the `imageView`(icon) shown on the side of `EditText`/`TextView` Also set the proper `EditText`/`TextView` Width `onCreate()` so it also covers when phone rotates. It accepts `LinearLayout` and `RelativeLayout` as parent, if parent is `null`, that means the parent is the phone screen itself.

* Course: All courses in WIT 2018 as `enum`. Please refer to my Gist on https://gist.github.com/John-Almardeny/54a4fcc43f8358ef88c9d2288a8a9cdf

* DoWithData: A workaround I developed to Execute Function(s)/Block of codes on specific data after fetching them from Database as that happen asynchronously so cannot be returned.

* DoWithDataException: Custom Exception for DoWithData Class.


**Highlights:**

* I developed an algorithm to set up the approximate destination size of a given image after compressing it from Bitmap to JPEG by practically calculating in reverse order the JPEG Depth used by Android. So it can encode different versions of bitmaps into `64Based String` that can be saved locally (in `SharedPreferences`) or on the cloud (as `Firebase` only allow for up to 10MB size as `String` value).

* The user has the option to Take a Selfie Picture or Upload a Picture from phone interactively via clicking on the profile image. 


**Database:**

* On-the-Cloud Firebase database contains -currently- one `Users` collection with many nodes sorted according to the unique `userId`, under each there is the user model in `JOSN` format.

* Authentication facility via using email and password.


**Rotation Support:**

This app supports phone rotation via having both portrait and landscape layouts.<br>
The appropriate layout is set according to the phone orientation at runtime by using a method in the Helper class that analyses the caller activity information and uses the `Reflection` to get the required fields from the `R` class which represent the layouts themselves.


**Approach Adopted for Persistence:**

I used the `SharedPreferences` to save and retrieve persistent key-value pairs of primitive data type for the user information and the different size of `64Based` Encoded Images.<br>
I followed most of the Android Lint suggestions to maintain persistent code style and improve performance in addition to locking and unlocking the views based on the type of the implementation (synchronous / asynchronous).<br>
The user can choose from a drop-down menu that contains all the available courses in WIT 2018 when they register. That will prevent dumb data and make it easier maintain consistency in Courses names and users' information. <br>
Also, everything is commented, so the code and implementation is self-explanatory.


**References:**

* Firebase Documentation at: https://firebase.google.com/docs/guides/

* Android Documentation at: https://developer.android.com/guide/index.html


