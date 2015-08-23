# Twitter Android App

Description
-----
This is an Android application that uses the Twitter API to retrieve home and mention timelines for a logged in user. It also allows the user to compose messages and view profiles of other users.

Technial Details
-----
- Android Async is used to make HTTP requests to Twitter API
- Images are retrieved by Picasso
- makeramen:roundedimageview is used to transform user photos to have rounded edges
- It has a custom onScrollListener to support "infinite" scrolling of tweets
- Users can sign in to Twitter using OAuth login
- astuetz:pagerslidingtabstrip is used to manage tabs

Walkthrough
---

![Video Walkthrough](twitter.gif)
