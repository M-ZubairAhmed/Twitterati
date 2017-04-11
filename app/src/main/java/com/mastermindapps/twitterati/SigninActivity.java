package com.mastermindapps.twitterati;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

public class SigninActivity extends AppCompatActivity {

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TwitterSecret.TWITTER_KEY, TwitterSecret.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_signin);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                successProceedNext(result);

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                Toast.makeText(SigninActivity.this, R.string.signin_problem, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    void successProceedNext(Result<TwitterSession> result) {
        TwitterSession session = result.data;
        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false).enqueue(new Callback<User>() {
            @Override
            public void failure(TwitterException e) {
                //If any error occurs handle it here
            }

            @Override
            public void success(Result<User> userResult) {
                User user = userResult.data;
                String userName = user.name;
                String userHandle = user.screenName;
                String profilePic = user.profileImageUrl;
                String coverPic = user.profileBannerUrl;
                Intent gotoHomeActivity = new Intent(SigninActivity.this, HomeActivity.class);
                gotoHomeActivity.putExtra("UserHandle", userHandle);
                gotoHomeActivity.putExtra("UserName", userName);
                gotoHomeActivity.putExtra("UserPic", profilePic);
                gotoHomeActivity.putExtra("UserCover",coverPic);
                startActivity(gotoHomeActivity);
                finish();
            }
        });
    }


}
