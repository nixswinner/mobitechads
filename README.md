# mobitechads
# Signup as developer at
https://mobitechads.com/home/monetize

# Mobitech Ads
Implements the mobitech ads

# Steps to add Ads

# Setup.
1.Add gradle dependency on build.gradle (app).
```
  implementation 'com.github.nixswinner:mobitechads:1.1.4'
```
2.Add on build.gradle project under all allprojects repositories.
```
allprojects {
    repositories {
       .....
       .....
        maven { url 'https://www.jitpack.io' }

    }
}

```

3.Android manifect make sure your Internet Permission.

```
  <uses-permission android:name="android.permission.INTERNET" />
```

# Add Banner ads.
1. On your xml layout 
```
<com.ads.mobitechadslib.MobiAdBanner
        android:id="@+id/bannerAd"
        android:layout_gravity="center"
        android:scaleType="fitXY"
        android:layout_width="320dp"
        android:layout_height="50dp"/>
```
2.On your activity logic.
  1.Find the view 
  ```
  MobiAdBanner bannerAd = findViewById(R.id.bannerAd);
  ```
  2.Load ads - specify ads category_id and pass the activity context applicationId - found at 
  ```
  bannerAd.getBannerAds(this,"applicationId","1");
  ```
  3.Auto Refresh banner - pass refresh rate (in minutes ) as an integer
  ```
   bannerAd3.getBannerAds(this, "1", 1); //refresh after 1 minute
  ```
  
  # Add Intertistial ad.
  
  Add the following code snippet on the activity logic
  It takes 3 parameters - context and ads category_id - string and your developer app id {get one at https://ads.mobitechads.com } - signup and create your app
  ```
   MobitechAds.getIntertistialAd(MainActivity.this,"applicationId","1");
  ```
  
