package com.example.project_health.user_interface

import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.project_health.R
import com.example.project_health.model.UserModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_main);

        //Communication with all the fragments
        val userAccount = this.intent.getParcelableExtra<UserModel>("UserObject") //WORKS

        //TODO ("Error with the bundle")
        val bundle = Bundle()
        bundle.putParcelable("UserAccount", userAccount)

        val setFragment = OverallFragment()
        val fm: FragmentTransaction = supportFragmentManager.beginTransaction()
        setFragment.arguments = bundle
        fm.replace(R.id.fragment_view, setFragment)
        fm.commit()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setNavigationItemSelectedListener {

            val id = it.itemId

            if(id == R.id.activitySummary) {
                val overallFragment = OverallFragment()
                val fm2: FragmentTransaction = supportFragmentManager.beginTransaction()
                overallFragment.arguments = bundle
                fm2.add(R.id.fragment_view, overallFragment)
                fm2.commit()
            }
            if(id == R.id.competition) {
                val competitionFragment = CompetitionFragment()
                val fm3: FragmentTransaction = supportFragmentManager.beginTransaction()
                competitionFragment.arguments = bundle
                fm3.add(R.id.fragment_view, competitionFragment)
                fm3.commit()
            }
            if(id == R.id.yourFriends) {
                val friendsFragment = FriendsFragment()
                val fm4: FragmentTransaction = supportFragmentManager.beginTransaction()
                friendsFragment.arguments = bundle
                fm4.add(R.id.fragment_view, friendsFragment)
                fm4.commit()
            }
            if(id == R.id.accountSettings) {
                val accountSettings = SettingsFragment()
                val fm5: FragmentTransaction = supportFragmentManager.beginTransaction()
                accountSettings.arguments = bundle
                fm5.add(R.id.fragment_view, accountSettings)
                fm5.commit()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}