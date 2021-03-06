package ie.wit.witselfiecompetition;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.Callable;

import ie.wit.witselfiecompetition.model.App;


/**
 * Splash Screen Class
 * Logo for the app
 * Created by Yahya Almardeny on 08/02/18.
 */
public class SplashScreen extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // check the phone orientation and set appropriate layout accordingly
        App.setContentAccordingToOrientation(this);


        // run asynchronously while displaying splash screen
        final Thread checkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(App.hasNetworkConnection(SplashScreen.this)) {
                    if(App.isLoggedInVerifiedUser(SplashScreen.this, false)) {
                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                        App.copyUserInfoFromDatabaseToSharedPref(SplashScreen.this, new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                if(App.sharedPreferencesDataExists(SplashScreen.this)){
                                    App.redirect(SplashScreen.this, Main.class, false);
                                }
                                else{
                                    App.redirect(SplashScreen.this, ProfileSetup.class, false);
                                }
                                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                return null;
                            }
                        });
                    }
                    else{
                        App.redirect(SplashScreen.this, Login.class, false);
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.showMessage(SplashScreen.this, "No Internet Connection!",
                                    "Please connect to Network and retry again", true);
                        }
                    });

                }
            }
        });

        // post event handler to move from current splash screen
        // activity to the proper next activity
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run () {
                checkThread.start();
            }
        }, 2000);
    }


    /**
     * This method is invoked upon
     * rotating the mobile phone
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        App.setContentAccordingToOrientation(this);
    }


}