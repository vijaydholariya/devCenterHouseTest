# devCenterHouseTest

This project task is select the music file from the storage .
splash screen:
     i add a handler and application version code by using package manager.getPackageInfo 
     val pInfo = applicationContext.packageManager?.getPackageInfo(
                applicationContext.packageName,
                0
            )
            versionName = pInfo?.versionName
            
            
MainActivity:
          1) view model: featch all music file from the storage and add it in recyleview :fetchLiveMusicData
          2)bottom sheet dialog to select the music file
          3)lifecycleScope coroutine scope 
          4) permission for access the music file from the storage
          5)add Listner callback to get the music interval and other data
