package com.example.chefchat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.chefchat.adapter.ProfilePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: ProfilePagerAdapter

    // UI elements
    private lateinit var profileImage: ImageView
    private lateinit var userName: TextView
    private lateinit var userBio: TextView
    private lateinit var tvFollowersCount: TextView
    private lateinit var tvFollowingCount: TextView
    private lateinit var tvRecipesCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        setupToolbar(user?.displayName ?: "Profile")
        initializeViews()
        setupViewPager()
        loadUserProfile(user?.uid)
    }

    private fun setupToolbar(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.profileToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(title)
        }
    }

    private fun initializeViews() {
        profileImage = findViewById(R.id.ivProfileImage)
        userName = findViewById(R.id.tvProfileName)
        userBio = findViewById(R.id.tvProfileBio)
        tvFollowersCount = findViewById(R.id.tvFollowersCount)
        tvFollowingCount = findViewById(R.id.tvFollowingCount)
        tvRecipesCount = findViewById(R.id.tvRecipeCount)
        viewPager = findViewById(R.id.profileViewPager)
        tabLayout = findViewById(R.id.profileTabLayout)
    }

    private fun setupViewPager() {
        pagerAdapter = ProfilePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Archive"
                1 -> "Liked"
                else -> ""
            }
        }.attach()
    }

    private fun loadUserProfile(userId: String?) {
        val user = auth.currentUser

        if (user != null) {
            // Load basic user info
            userName.text = user.displayName ?: "Chef User"
            userBio.text = "Passionate about cooking. Love sharing food experiences!"

            // Load profile image
            Picasso.get()
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_profile_chef)
                .error(R.drawable.ic_profile_chef)
                .into(profileImage)

            // Load user stats from Firestore
            loadUserStats(userId)
        }
    }

    private fun loadUserStats(userId: String?) {
        if (userId == null) return

        // Load followers count
        firestore.collection("users")
            .document(userId)
            .collection("followers")
            .get()
            .addOnSuccessListener { documents ->
                tvFollowersCount.text = documents.size().toString()
            }
            .addOnFailureListener {
                tvFollowersCount.text = "0"
            }

        // Load following count
        firestore.collection("users")
            .document(userId)
            .collection("following")
            .get()
            .addOnSuccessListener { documents ->
                tvFollowingCount.text = documents.size().toString()
            }
            .addOnFailureListener {
                tvFollowingCount.text = "0"
            }

        // Load recipes count
        firestore.collection("recipes")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                tvRecipesCount.text = documents.size().toString()
            }
            .addOnFailureListener {
                tvRecipesCount.text = "0"
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_settings -> {
                openSettings()
                true
            }
            R.id.action_edit_profile -> {
                editProfile()
                true
            }
            R.id.action_share_profile -> {
                shareProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettings() {
        // TODO: Navigate to settings activity
        Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
        // val intent = Intent(this, SettingsActivity::class.java)
        // startActivity(intent)
    }

    private fun editProfile() {
        // TODO: Navigate to edit profile activity
        Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        // val intent = Intent(this, EditProfileActivity::class.java)
        // startActivity(intent)
    }

    private fun shareProfile() {
        val shareText = "Check out my ChefChat profile: ${userName.text}"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Profile"))
    }
}