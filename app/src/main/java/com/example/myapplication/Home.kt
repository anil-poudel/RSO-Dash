package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_page.*

class Home : AppCompatActivity() {


    //objects for fragments
    lateinit var dashboardFragment: DashboardFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var bulletinboardFragment: BulletinboardFragment
    var selected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        //BottomNavigationView
        val bottomNavigation: BottomNavigationView = findViewById(R.id.btm_nav)

        //SET dashboard as default fragment
        dashboardFragment = DashboardFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, dashboardFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        //When selected each items, update the fragment view
        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.dashboard -> {
                    dashboardFragment = DashboardFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, dashboardFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }



                R.id.calendar -> {
                    calendarFragment = CalendarFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, calendarFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }



                R.id.bulletin -> {
                    bulletinboardFragment = BulletinboardFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, bulletinboardFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

            }
            true


        }
        //starts calendar activity on calendar button tap
        calendar_button.setOnClickListener {
            val intent = Intent(this, Calendar::class.java)
            startActivity(intent)
        }

        //TODO:REMOVE DATABASE TESTING ACTIVITY, this is meant to build the database
        todo_list_button.setOnClickListener {
            val intent = Intent(this, DatabaseTesting::class.java)
            startActivity(intent)
        }

        //signs out user when sign_out button is pressed
        sign_out_button.setOnClickListener {
           signOutUser()

        }
    }


    //For ToolBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu,menu);
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.settings) {
            if(!selected) {



                maincontent.animate().translationX(0F)
                leftdrawer.animate().translationX(0F)

                selected = true
            }
            else if(selected){
                maincontent.animate().translationX(-735F)
                leftdrawer.animate().translationX(-735F)

                selected = false
            }

            return true
        }
        return super.onOptionsItemSelected(item)
    }





    //this function signs the current user out
    private fun signOutUser(){
        //gets instance of the Firestore database
        val db = FirebaseFirestore.getInstance()
        //save the current user's uid, which will be used to grab the user's document from Firestore database
        val uid = FirebaseAuth.getInstance().uid.toString()
        //grab reference to the user's document from Firestore database
        val docRef = db.collection("users").document(uid)

        //if the reference is successfully grabbed then sign the user out and display their name
        docRef.get().addOnSuccessListener {
            //save the user's name from the document snapshot "it"
            val name = it.getString("firstName")
            //display a sign out message showing the user's name and then sign the user out
            //through the firebase authenticator
            Toast.makeText(this, "Signing out $name", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        //if the user fails to sign out, display error message to the user
        }.addOnFailureListener {
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


}