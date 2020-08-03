package guide.graphql.toc.ui.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.transition.MaterialSharedAxis
import guide.graphql.toc.SectionsQuery
import guide.graphql.toc.data.Apollo
import guide.graphql.toc.databinding.SectionsFragmentBinding

class SectionsFragment : Fragment() {


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

        lifecycleScope.launchWhenStarted {
            binding.spinner.visibility = View.VISIBLE
            binding.error.visibility = View.GONE
            try {
                val response = Apollo.client.query(
                    SectionsQuery(id = args.chapterId)
                ).toDeferred().await()

                if (response.hasErrors()) {
                    throw Exception("Response has errors")
                }

                val sections = response.data?.chapter?.sections ?: throw Exception("Data is null")

                if (sections.size > 1) {
                    adapter.updateSections(sections)
                    binding.spinner.visibility = View.GONE
                    binding.error.visibility = View.GONE
                } else {
                    throw Exception("No sections")
                }
            } catch (e: ApolloException) {
                showErrorMessage("GraphQL request failed")
            } catch (e: Exception) {
                showErrorMessage(e.message.orEmpty())
            }
        }
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