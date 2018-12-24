package dk.dampbiksen.community.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import dk.dampbiksen.community.ActivityMain;
import dk.dampbiksen.community.R;
import dk.dampbiksen.community.models.DiscountEntry;
import dk.dampbiksen.community.models.NewsEntry;
import dk.dampbiksen.community.models.PollEntry;
import dk.dampbiksen.community.util.FirebaseCallback;

public class ActivityFacebookLogin extends AppCompatActivity {

    private enum PendingAction {
        NONE,
        LOG_IN,
        LOG_OUT,
        POST_PHOTO,
        POST_STATUS_UPDATE

    }
    private final String PENDING_ACTION_BUNDLE_KEY = "dk.dampbiksen.community.login:PendingAction";
    private static final String PERMISSION = "publish_actions";
    private static final Location SEATTLE_LOCATION = new Location("") {
        {
            setLatitude(47.6097);
            setLongitude(-122.3331);
        }
    };
    private ProfilePictureView profilePictureView;
    private TextView greeting;
    private MaterialButton login_next;
    private PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("MyCalls", "DB facelogin - Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("MyCalls", String.format("DB facelogin"+" Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("MyCalls", "DB facelogin "+"Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);

            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(ActivityFacebookLogin.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handlePendingAction();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        updateUI();

                    }

                    @Override
                    public void onCancel() {
                        if (pendingAction != PendingAction.NONE) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (pendingAction != PendingAction.NONE
                                && exception instanceof FacebookAuthorizationException) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(ActivityFacebookLogin.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.layout_activity_login_facebook);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.
                handlePendingAction();
            }
        };



        profilePictureView = findViewById(R.id.profilePicture);
        greeting = findViewById(R.id.greeting);
        login_next = findViewById(R.id.fb_next_button);

        // Grab button and set OnClickListener.
        // Set an error if the password is less than 8 characters.
        login_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getVisibility() == View.VISIBLE)
                {
                    if(Profile.getCurrentProfile() != null)
                    {
                        proceed();
                    }
                }

            }
        });

        // Can we present the share dialog for regular links?
        canPresentShareDialog = ShareDialog.canShow(
                ShareLinkContent.class);

        // Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = ShareDialog.canShow(
                SharePhotoContent.class);

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        LoginManager.getInstance().unregisterCallback(callbackManager);
    }

    public  void updateUI()
    {
        boolean enableButtons = AccessToken.isCurrentAccessTokenActive();
        Profile profile = Profile.getCurrentProfile();

        if (enableButtons && profile != null) {
            profilePictureView.setProfileId(profile.getId());
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
            login_next.setVisibility(View.VISIBLE);
        } else {
            login_next.setVisibility(View.INVISIBLE);
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }

    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:

                break;
            case LOG_IN:

                break;
            case LOG_OUT:

                break;
            case POST_PHOTO:

                break;
            case POST_STATUS_UPDATE:

                break;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        final AuthCredential credential = FacebookAuthProvider.getCredential((token.getToken()));
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null)
                            {
                                Toast.makeText(ActivityFacebookLogin.this, "Authentication Succeded.",
                                        Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(ActivityFacebookLogin.this, "Authentication halfpassed - User = Null.",
                                        Toast.LENGTH_LONG).show();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ActivityFacebookLogin.this, "Authentication Failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void proceed()
    {
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }


    //Facebook Functions

    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }

    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Dampbiksen.Dk Test")
                .setContentDescription(
                        "Created using the FB developer documentation")
                .setContentUrl(Uri.parse("http://developers.facebook.com/docs/android"))
                .build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    private void onClickPostPhoto() {
        performPublish(PendingAction.POST_PHOTO, canPresentShareDialogWithPhotos);
    }

    private void postPhoto() {
        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon);
        SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(image).build();
        ArrayList<SharePhoto> photos = new ArrayList<>();
        photos.add(sharePhoto);

        SharePhotoContent sharePhotoContent =
                new SharePhotoContent.Builder().setPhotos(photos).build();
        if (canPresentShareDialogWithPhotos) {
            shareDialog.show(sharePhotoContent);
        } else if (hasPublishPermission()) {
            ShareApi.share(sharePhotoContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_PHOTO;
            // We need to get new permissions, then complete the action when we get called back.
            LoginManager.getInstance().logInWithPublishPermissions(
                    this,
                    Arrays.asList(PERMISSION));
        }
    }

    private boolean hasPublishPermission() {
        return AccessToken.isCurrentAccessTokenActive()
                && AccessToken.getCurrentAccessToken().getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {
        if (AccessToken.isCurrentAccessTokenActive() || allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }



}
