package guide.graphql.toc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import guide.graphql.toc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navigationListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        if (destination.id == R.id.chapters_fragment) {
            this.setTitle(R.string.app_name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.nav_host_fragment))

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener(navigationListener)
    }

    override fun onPause() {
        super.onPause()
        findNavController(R.id.nav_host_fragment).removeOnDestinationChangedListener(navigationListener)
    }
}
