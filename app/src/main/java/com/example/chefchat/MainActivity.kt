package com.example.chefchat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        // Set app title and colors
        toolbar.title = "ChefChat"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.chef_white))
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                showUserProfileDialog()
                true
            }
            R.id.menu_edit_profile -> {
                showEditProfileOptions()
                true
            }
            R.id.menu_logout -> {
                showLogoutConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showUserProfileDialog() {
        val user = auth.currentUser
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_profile, null)
        val nameView = dialogView.findViewById<TextView>(R.id.tvUserName)
        val emailView = dialogView.findViewById<TextView>(R.id.tvUserEmail)
        val imageView = dialogView.findViewById<ImageView>(R.id.ivProfilePic)

        nameView.text = user?.displayName ?: "No Name"
        emailView.text = user?.email ?: "No Email"

        val photoUrl: Uri? = user?.photoUrl
        if (photoUrl != null) {
            Picasso.get()
                .load(photoUrl)
                .placeholder(R.drawable.ic_profile_chef)
                .error(R.drawable.ic_profile_chef)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_profile_chef)
        }

        AlertDialog.Builder(this)
            .setTitle("Chef Profile")
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .apply {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                    ContextCompat.getColor(this@MainActivity, R.color.chef_orange)
                )
            }
    }

    private fun showEditProfileOptions() {
        val options = arrayOf("Change Display Name", "Update Profile Picture", "Manage Preferences")

        AlertDialog.Builder(this)
            .setTitle("Edit Chef Profile")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Change Display Name - Coming Soon!", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "Update Profile Picture - Coming Soon!", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "Manage Preferences - Coming Soon!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .apply {
                show()
                getButton(AlertDialog.BUTTON_NEGATIVE)
                    ?.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.chef_orange))
            }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout from ChefChat?")
            .setIcon(R.drawable.ic_logout_chef)
            .setPositiveButton("Yes, Logout") { _, _ ->
                signOutUser()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .apply {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE)
                    ?.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.chef_orange))
                getButton(AlertDialog.BUTTON_NEGATIVE)
                    ?.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.chef_text_light))
            }
    }

    private fun signOutUser() {
        // Firebase logout
        auth.signOut()

        // Google logout
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        GoogleSignIn.getClient(this, gso).signOut()

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
