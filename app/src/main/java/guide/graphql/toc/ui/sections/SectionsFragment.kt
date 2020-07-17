package guide.graphql.toc.ui.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.transition.MaterialSharedAxis
import guide.graphql.toc.databinding.SectionsFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SectionsFragment : Fragment() {

    private val viewModel: SectionsViewModel by viewModels()

    private var _binding: SectionsFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val args: SectionsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SectionsFragmentBinding.inflate(inflater, container, false)
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

        viewModel.sectionList.observe(viewLifecycleOwner, Observer { sections ->
            Log.i("SectionsFragment", "Updated section list")
            adapter.submitList(sections)
            binding.spinner.visibility = View.GONE
            binding.error.visibility = View.GONE
        })

        viewModel.sectionException.observe(viewLifecycleOwner, Observer {
            it?.let { exception ->
                if (exception is ApolloException) {
                    showErrorMessage("GraphQL request failed")
                } else {
                    showErrorMessage(exception.message ?: "")
                }
            }
        })

        viewModel.chapterId = args.chapterId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorMessage(error: String) {
        binding.spinner.visibility = View.GONE
        binding.error.text = error
        binding.error.visibility = View.VISIBLE
    }
}