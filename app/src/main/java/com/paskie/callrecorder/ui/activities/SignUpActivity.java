package com.paskie.callrecorder.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.paskie.callrecorder.R;
import com.paskie.callrecorder.utils.Pref;
import com.paskie.callrecorder.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created By Adigun Hammed Olalekan
 * 7/12/2017.
 * Beem24, Inc
 */

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.email_edt_sign_up)
    EditText mailEditText;
    @BindView(R.id.edt_username_sign_up)
    EditText usernameEditText;
    @BindView(R.id.edt_password_sign_up)
    EditText passwordEditText;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        progressDialog = new ProgressDialog(this);
    }
    @OnClick(R.id.btn_sign_up_sign_up) public void onSignUpClick() {
        if(!Patterns.EMAIL_ADDRESS.matcher(Util.text(mailEditText)).matches()) {
            showDialog("Error", "Invalid email address.");
            return;
        }
        if(Util.empty(usernameEditText)) {
            showDialog("Error", "Please enter a username.");
            return;
        }
        if(Util.empty(passwordEditText)) {
            showDialog("Error", "Invalid password");
            return;
        }
        if(!Util.online(this)) {
            showDialog("Error", "Device is offline");
            return;
        }
        Util.hideKeyboard(this);
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", Util.text(mailEditText));
        requestParams.put("password", Util.text(passwordEditText));
        requestParams.put("username", Util.text(usernameEditText));

        Util.asyncHttpClient.post(Util.URL + "/register", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showDialog("Error", "Network response unknown. Please retry.");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Pref.getInstance().save(jsonObject.getJSONObject("data"));

                    Intent intent = new Intent(SignUpActivity.this, Main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (JSONException je) {}

            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.cancel();
            }

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }
        });
    }
}
