package guide.graphql.toc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    private val navigationListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        if (destination.id == R.id.chapters_fragment) {
            this.setTitle(R.string.app_name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
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
