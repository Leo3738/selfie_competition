package ie.wit.witselfiecompetition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import ie.wit.witselfiecompetition.model.User;

import static android.content.Context.MODE_PRIVATE;


/**
 * This Helper Class to accelerate and ease the work
 * and compact it, also to avoid loads of duplicates
 * Created by Yahya on 20/02/18.
 */

public class Helper {


    /**
     * This method set the appropriate layout
     * based one the orientation of the mobile phone (Lnadscape vs Portrait)
     * Also this method is smart to link the appropriate layout to its activity
     */
    public static void setContentAccordingToOrientation(Activity activity){

        String portraitFieldName = "activity_".concat(activity.getClass().getSimpleName().toLowerCase()).concat("_portrait");
        String landscapeFieldName  = "activity_".concat(activity.getClass().getSimpleName().toLowerCase()).concat("_landscape");
        try{
            int portrait = R.layout.class.getField(portraitFieldName).getInt(null);
            int landscape = R.layout.class.getField(landscapeFieldName).getInt(null);
            if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                activity.setContentView(portrait);
            }
            else{
                activity.setContentView(landscape);
            }
        }catch (Exception e){
            Log.e("set content", e.getMessage());
        }
    }


    /**
     * This method checks if there is internet connection
     * @param context
     * @return
     */
    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    /**
     * This method to validate the email input
     * EMAIL_ADDRESS pattern is built in Patterns Class starting with API Level 8
     * @param emailEditText
     * @return
     */
    public static boolean isValidEmail (EditText emailEditText){
        String email = emailEditText.getText().toString().trim();
        if(email.isEmpty()){
            emailEditText.setError(Html.fromHtml("<font color='white'>Email can't be empty!</font>"));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(Html.fromHtml("<font color='white'>Invalid Email!</font>"));
            return false;
        }
        return true;
    }


    /**
     * This method to validate the password input
     * Password can't be empty and must be at least 8 characters long
     * must contain at least one digit and alphabet character
     * @param passwordEditText
     * @return
     */
    public static boolean isValidPassword (EditText passwordEditText){
        String password = passwordEditText.getText().toString().trim();
        if(password.isEmpty()){
            passwordEditText.setError(Html.fromHtml("<font color='white'>Password can't be empty</font>"));
            return false;
        }
        if (!(password.length()>=8) || !Pattern.compile("[0-9]").matcher(password).find() ||
                !Pattern.compile("[a-zA-z]").matcher(password).find()) {
            passwordEditText.setError(Html.fromHtml("<font color='white'>Password must be at least 8 characters long, " +
                    "contain at least one digit and alphabet character</font>"));
            return false;
        }
        return true;
    }


    /**
     * Generic method to display a popup dialog
     * @param activity
     * @param title
     * @param message
     */
    public static void showMessage(final Activity activity, String title, String message, final boolean finishParent){
        AlertDialog.Builder dialog  = new AlertDialog.Builder(activity, R.style.alertDialog);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(finishParent) {activity.finish();}
            }
        });
        dialog.create().show();
    }


    /**
     * This method to move to another activity after showing a message
     * @param activity
     * @param targetClass
     * @param title
     * @param message
     * @param backable
     */
    public static void redirectWithMessage(final Activity activity, final Class targetClass, String title, String message, final boolean backable){
        AlertDialog.Builder dialog  = new AlertDialog.Builder(activity, R.style.alertDialog);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                redirect(activity, targetClass, backable);
            }
        });
        dialog.create().show();
    }


    /**
     * Toggle visibility between given view and progressbar
     * @param view
     * @param progressBar
     */
    public static void toggleProgressBar(View view, ProgressBar progressBar){
        if(view.getVisibility() == View.INVISIBLE){
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {
            view.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    /**
     * This method to move conditionally between activities
     * @param activity
     * @param targetClass
     * @param backable
     */
    public static void redirect(Activity activity, Class targetClass, boolean backable){
        Intent intent = new Intent(activity.getApplicationContext(), targetClass);
        if(!backable){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            activity.finish();
            activity.startActivity(intent);
            return;
        }
        activity.startActivity(intent);
    }


    /**
     * This method to hide the soft keyboard on request
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(new View(activity).getWindowToken(), 0);
    }


    /**
     * Check if the current user exists and verified
     * @return
     */
    public static boolean isLoggedInVerifiedUser(Activity activity, boolean message){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                if (message) {
                    Toast.makeText(activity, "This account is not Verified\nCheck your Inbox", Toast.LENGTH_LONG).show();
                }
                FirebaseAuth.getInstance().signOut();
                return false;
            }
            return true;
        }
        return false;
    }



    /**This method to check if the given name is valid
     * and probably a real one
     * @param activity
     * @param nameEditText
     * @param field
     * @return
     */
    public static boolean isValidName(Activity activity, EditText nameEditText, String field){
        String name = nameEditText.getText().toString().trim();
        if(name.isEmpty()){
            nameEditText.setError(field + " cannot be empty");
            return false;

        }
        for(char c : name.toCharArray()){
            if(!Character.isLetter(c) && !Character.isSpaceChar(c)){
                Toast.makeText(activity, "Please insert your real "+field,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    /**
     * This method checks with database
     * looks for user id
     * if it doesn't exist, that means it's the first login
     * @param from
     * @param one
     * @param two
     * @return
     */
    public static void firstLoginCheck(final Activity from, final Class one, final Class two) {
        FirebaseDatabase.getInstance().getReference().child("Users").orderByKey().
                equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Helper.redirect(from, one, false);
                        } else {
                            Helper.redirect(from, two, false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(from, "Failed to verify log in", Toast.LENGTH_LONG);
                    }
                });
    }


    /**
     * This method to add to SharedPreferences file
     * @param activity
     * @param map
     */
    public static void addToSharedPreferences(Activity activity, Map<String,?> map) {
        String name = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        for (Map.Entry<String,?> entry : map.entrySet()) {
            Object v = entry.getValue();
            String k = entry.getKey();

            if(v instanceof String){
                editor.putString(k,(String)v).commit();
            }
            else if (v instanceof Integer){
                editor.putInt(k, (Integer)v).commit();
            }
            else if (v instanceof Float){
                editor.putFloat(k, (Float)v).commit();
            }
            else if (v instanceof Long){
                editor.putLong(k, (Long)v).commit();
            }
            else if (v instanceof Boolean){
                editor.putBoolean(k, (Boolean)v).commit();
            }
        }

    }






    /**
     * Check and Ask for permission to write to phone storage
     * @param activity
     * @param PERMISSION_CODE
     */
    public static boolean grantPermission(Activity activity, final int PERMISSION_CODE){
       boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_CODE);
        }
        return hasPermission;
    }


    /**
     * Conditionally set the user image and full name after login
     * but from the cloud (database)
     * @param fullName
     * @param profileImage
     */
    public static void setPersonalImageAndNameFromDB(final TextView fullName, final ImageView profileImage){
        FirebaseDatabase.getInstance().getReference().child("Users").orderByKey().
                equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                            String firstName = user.getfName();
                            String lastName = user.getlName();
                            String encodedImage = user.getImage();
                            String gender = user.getGender();
                            fullName.setText(firstName + " " + lastName);

                            if (encodedImage.isEmpty()) {
                                switch (gender) {
                                    case "Male":
                                        profileImage.setImageResource(R.drawable.male);
                                        break;
                                    case "Female":
                                        profileImage.setImageResource(R.drawable.female);
                                        break;
                                }
                            } else {
                                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                profileImage.setImageBitmap(bitmap);
                            }
                        }catch (Exception e){
                            Log.e("Set Profile", e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }


    /**
     * Conditionally set the user image and full name after login
     * from the sharedPreferences (local version of data)
     * @param fullName
     * @param profileImage
     */
    public static void setPersonalImageAndName(Activity activity, TextView fullName, ImageView profileImage){
        String name = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
        fullName.setText(pref.getString("fName", "") + " " + pref.getString("lName", ""));
        String gender = pref.getString("gender", "");
        String encodedImage = pref.getString("image", "");
        if (encodedImage.isEmpty()) {
            switch (gender) {
                case "Male":
                    profileImage.setImageResource(R.drawable.male);
                    break;
                case "Female":
                    profileImage.setImageResource(R.drawable.female);
                    break;
            }
        } else {
            profileImage.setImageBitmap(decodeImage(encodedImage));
        }
    }



    /**
     * Add a given data to database
     * data in format of map
     * keys are fields names
     * values are the data of the fields
     * @param activity
     * @param node
     * @param data
     * @param errorMessage
     */
    public static void addToDatabase(final Activity activity, String node, Map<String,?> data, final String errorMessage){
        for (Map.Entry<String,?> entry :data.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            FirebaseDatabase.getInstance().getReference().child(node)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(field).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        if(errorMessage!=null){
                            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


    /**
     * Encode a given uri of image to a Base64 string
     * @param activity
     * @param imageUri
     * @param thumbnail
     * @return
     */
   public static String encodeImage(final Activity activity, Uri imageUri, boolean thumbnail){
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);

            if(thumbnail){
                int dstWidth = 500;
                int dstHeight = (int)((float)dstWidth/((float)bitmap.getWidth()/(float)bitmap.getHeight()));
                bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);

        } catch (Exception e){
            Log.e("encode image", e.getMessage());
            return null;
        }
   }



    /**
     * Decode a given encoded image (string) to bitmap
     * @param encodedImage
     * @return
     */
   public static Bitmap decodeImage(String encodedImage) {
       byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
       Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
       return bitmap;
   }



    /**
     * Ask for specific field's value and get it
     * @param activity
     * @param field
     * @param type
     * @return
     */
   public static Object getFromSharedPreferences(Activity activity, String field, String type){
       String name = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
       SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);

       switch (type){
           case "String":
               return pref.getString(field, null);
           case "Integer":
               return pref.getInt(field, 0);
           case "Float":
               return pref.getFloat(field, 0.f);
           case "Long":
               return pref.getLong(field, 0);
           case "Boolean":
               return pref.getBoolean(field, false);
           default:
               return null;
       }
   }


    /**
     * Clear sharedPreferences on request
     * @param activity
     */
    public static void clearSharedPreferences(Activity activity){
        String name = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().commit();
    }


    /**
     * Return the user information in a map
     * @return
     */
    public static Map<String,String> getInfoInMap(User user){
        Map<String, String> profileData = new HashMap<>();
        profileData.put("fName", user.getfName());
        profileData.put("lName", user.getlName());
        profileData.put("gender", user.getGender());
        profileData.put("course", user.getCourse());
        profileData.put("image", user.getImage());
        return profileData;
    }
}
