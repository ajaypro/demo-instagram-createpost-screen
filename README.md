# Creating a Home screen containing list of posts 
  
  ### requirements 
  
  * Appbar containing logo/app name 
  * User pic and user name
  * recent post
  * time of post
  * no of likes for the post if user is logged in.
  
  
  ### Approach
  
  ### Base setup
  
  * Initally we create containers that will have fragments and navigation as well
  * Creating placeholders that will heave a ui and viewmodel main, home, photo, profile
  * Lazy loading of fragments that is loading the fragments from saved so that new instance of fragment is not loaded each time
    e.g if i click profile -> profile fragment will be loaded and it will be saved in mainactivity so that next time when it clicked new instance 
	is not created. 
  * mainactivity -> tell mainviewmodel what is selected on bottom navigation and viewmodel will decide which fragment to be loaded. 
  * Create livedata in viewmodel that keeps track of the navigation of fragments from viewmodel which is later been observed by mainactivity
  
  ### Functionality building
  
  * First api to list the posts
  * Call to like api and unlike api
  * First we need to work on 
			data layer 
			  * Create post model
			  * create inner user class in post model, this is different user class that is associated with post data as the info is different 
			    from actual User model, so we have created a separate inner class for User
			Business logic layer 
			UI layer
  * Now to make the first feature display posts in the home fragment 
  * we need 
      * Home Fragment -> will host the recyclerview, set the posts list obtained from posts livedata from viewmodel into the adapter
	  * HomeViewModel -> will have live data to show data loading and list of posts and paginator to load more data when user scrolls down 
	  * PostAdapter -> will have list of posts
  * Now we need to display the post in the ui and also make api call for like and dislike on the post. 
  * We will also load image for image post using glide and the url that we use in glide should be authenticated
  