package guide.graphql.toc.ui.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import guide.graphql.toc.*
import guide.graphql.toc.databinding.SectionsFragmentBinding

class SectionsFragment : Fragment() {

    private val viewModel: SectionsViewModel by viewModels()

    private lateinit var binding: SectionsFragmentBinding
    val args: SectionsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SectionsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val forward = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = forward

        val backward = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = backward
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter =
            SectionsAdapter(
                requireContext(),
                args.chapterNumber
            )
        val layoutManager = LinearLayoutManager(requireContext())
        binding.sections.layoutManager = layoutManager
        val itemDivider =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.sections.addItemDecoration(itemDivider)
        binding.sections.adapter = adapter

        viewModel.sectionsList.observe(viewLifecycleOwner, Observer { sectionsResource ->
            when (sectionsResource.status) {
                Status.SUCCESS ->  {
                   sectionsResource.data?.let {
                       adapter.updateSections(it)
                       binding.spinner.visibility = View.GONE
                       binding.error.visibility = View.GONE
                   }
                }
                Status.ERROR -> {
                    showErrorMessage(sectionsResource.message?: "")
                }
                Status.LOADING -> {
                    binding.spinner.visibility = View.VISIBLE
                    binding.error.visibility = View.GONE
                }
            }
         })


        viewModel.chapterId = args.chapterId

    }

    private fun showErrorMessage(error: String) {
        binding.spinner.visibility = View.GONE
        binding.error.text = error
        binding.error.visibility = View.VISIBLE
    }
}