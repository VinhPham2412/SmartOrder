package com.example.su21g3project;

import androidx.test.core.app.ActivityScenario;

import com.example.su21g3project.General.LoginActivity;
import com.google.firebase.auth.FirebaseAuthException;
import org.junit.Test;

public class UnitTest {
    @Test(expected = FirebaseAuthException.class)
    public void login_invalidPhone(){
        try(ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
            scenario.onActivity(activity ->{
                activity.getPhone().setText("0878");
                assert(activity.getBtnLogin().performClick());
            });
        }
    }
}
