# YouTube android App + server

How to run our app:
1. install android studio
2. clone the repository
3. go to the res -> values -> string.xml and change the baseUrl to your IP
4. run the server (see the server section for how to run it)
5. connect your android device
6. click run and open the app
7. allow all the permission

workflow:
First we converted the architecture of the application to the MVVM architecture, then we added support for ROOM in that the information about the videos and the user were saved locally on the device and then we connected the application to the server with the help of retrofit. And finally we fixed some bugs so that everything works together properly. 
Regarding the work regarding the server, we divided the work between us that one worked to ensure that the information goes from the application to the server and is processed as required, and the other worked to ensure that the information from the server reaches the application and is used properly.

**important Note:**  this app doesnt support accsess to images and videos from google photos, so only use the default gallery app.

## Permissions
The app requires the following permissions:

1. READ_MEDIA_VIDEO
2. READ_MEDIA_IMAGES
3. READ_EXTERNAL_STORAGE
4. CAMERA

These permissions are requested at runtime. If the permissions are not granted, the app will not function. Each time the app is activated, it will check for these permissions.
for the app to run these permissions must to be granted.

## server

How to run our server:
