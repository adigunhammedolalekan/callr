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

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_email_callr_login)
    EditText emailEditText;
    @BindView(R.id.edt_password_login)
    EditText passwordEditText;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        progressDialog = new ProgressDialog(this);
    }
    @OnClick(R.id.btn_create_account) public void onCreateAccountClick() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_sign_in) public void onSignInClick() {
        if(!Patterns.EMAIL_ADDRESS.matcher(Util.text(emailEditText)).matches()) {
            showDialog("Error", "Invalid email address");
            return;
        }
        if(Util.empty(passwordEditText)) {
            showDialog("Error", "Please enter your password.");
            return;
        }
        if(!Util.online(this)) {
            showDialog("Error", "Device is offline");
            return;
        }
        Util.hideKeyboard(this);
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", Util.text(emailEditText));
        requestParams.put("password", Util.text(passwordEditText));

        Util.asyncHttpClient.post(Util.URL + "/login", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showDialog("Error", "Network response unknown. Please retry.");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Pref.getInstance().save(jsonObject.getJSONObject("data"));

                    Intent intent = new Intent(LoginActivity.this, Main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (JSONException je) {}
            }

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.cancel();
            }
        });
    }
}
