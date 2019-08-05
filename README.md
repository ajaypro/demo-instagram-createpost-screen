## Create photo post
 
 * why are we saving the inputfile from gallery 
 * why getimagesize 
 * why using new list after refreshing
 
 ### upload photo and create post
 
 ### Upload image
 
   * When add photo is clicked it will go to photofragment which will have options like camera and gallery to fetch image.
   * Camera
     * In `onActivityResult` when we receive requestcode for camera, we pass it to viewmodel to handle it by calling `onCameraImageTaken`
	    which takes a lambda of camera's bitmap path 
	 * In function `onCameraImageTaken`, since we are fetching the image path we fetch it form background thread using `Single.fromCallable{}`
	  
	  ```
	  Single.fromCallable { camerImageProcessor() }
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    File(it).apply {
                        FileUtils.getImageSize(this)
                    }
                }
	  ```
	  * and the result of is a file using which we get the imageSize
	  
	### Gallery 
	
	  * In `onActivityResult` when we receive requestcode for gallery, we pass it to viewmodel to handle it by calling `onGalleryImageSelected`
	    which takes `inputstream` as argument, here we are compressing, decoding using bitmapfactory and converting the image as required size
		which returns a file of the image
		
		```
		Single.fromCallable {
                FileUtils.saveInputStreamToFile(inputStream, directory, "sample-img", 500)
            }.subscribeOn(schedulerProvider.io())
                .subscribe({
                    if (it != null) {
                        FileUtils.getImageSize(it)?.let { size ->
                            uploadAndCreatePost(it, size)
                        }
                    } else {
                        loading.postValue(false) // progressbar loading
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }
		
		```
 * create post 
 * Navigating between fragments through activity and also after uploading the post it needs to show `homefragment` with a list of recent posts
 * To achieve this I created a SharedViewmodel which will be used across `homefragment` `photofragment` and `mainactivity`
  ```
 
    MainSharedViewModel 
	
	 val homeRedirection: MutableLiveData<Boolean> = MutableLiveData()
    val newPost: MutableLiveData<Event<Post>> = MutableLiveData()

    fun onHomeRedirect() {
         homeRedirection.postValue(true)
    }
	
	```
  * Here we have livedata for redirectiong to `homefragment` and also newpost which will be observed in `photofragment` by `posts` livedata of 
     `photoViewModel`'s that will display it in the fragment. 
  * Now we redirect to `mainactivity` after creating the new post, which actually should host `homefragment` with latest posts
  * In the `mainactivity` we do manual redirect of of clicking the `homefragment` option  so that it displays `homefragment`
    * To display recent posts with newpost at the top of list in the `homefragment` we do two things 
	  update the base adapter with new list by clearing the old list( will try diff util and update as well) and the `homefragment` will 
	  call the `onNewPost()` from `homeviewmodel` which is observed by livedata of `sharedviewmodel`
	* In the `onNewPost()` we will update the postlist with new post at 0th index so it will the first post and refresh the postlist 