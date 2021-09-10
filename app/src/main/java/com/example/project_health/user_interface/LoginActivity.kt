package com.example.project_health.user_interface

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.project_health.R

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_main);

        val loginFragment = LoginFragment()
        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().add(R.id.fragment_view, loginFragment).commit()
    }
}