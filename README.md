# YouTube android App + server

How to run our app:
1. install android studio
2. clone the repository
3. go to the res -> values -> strings.xml and change the baseUrl to the IP of your server
4. run the server (see the server section for how to run it)
5. connect your android device
6. click run and open the app
7. allow all the permission

workflow:
First we converted the architecture of the application to the MVVM architecture, then we added support for ROOM in that the information about the videos and the user were saved locally on the device and then we connected the application to the server with the help of retrofit. And finally we fixed some bugs so that everything works together properly. 
Regarding the work on the server, we divided the work between us that one worked to ensure that the information goes from the application to the server and is processed as required, and the other worked to ensure that the information from the server reaches the application and is used properly.

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

  1.Navigate the the server folder using cd server/backend

  2.create a file called .env with the following fields: MONGO_URI: (the mongodb connection uri) PORT: (we used 5000) ACCESS_TOKEN_SECRET: (token for jwt , we used a random 64 hex             refer to ##Token to generate one)
  
  3.Use npm start to start the server

  

## mongo file integration
How to upload our already exisitng data into your mongo database:

  1.Navigate to server and open file importData.js
  
  2.replace const uri = "" with your mongo connection uri
  
  3.change const db = client.db('test') to the name of your database
  
  4.run importData.js using --  node importData.js  --  (in the server folder)

## Token 
How to create a random token:

  1.open a new terminal and insert the following:'' node require('crypto').randomBytes(64).toString('hex') '' then change the ACCESS_TOKEN_SECRET with the result
