package guide.graphql.toc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.transition.MaterialSharedAxis
import guide.graphql.toc.databinding.SectionsFragmentBinding

class SectionsFragment : Fragment() {

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
        val forward = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        enterTransition = forward

        val backward = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = backward
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.chapterHeader)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.chapterHeader.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launchWhenResumed {
            binding.spinner.visibility = View.VISIBLE
            binding.error.visibility = View.GONE

            val response = try {
                apolloClient.query(
                    SectionsQuery(id = args.chapterId)
                ).toDeferred().await()
            } catch (e: ApolloException) {
                showErrorMessage(getString(R.string.graphql_error, e.message))
                return@launchWhenResumed
            }

            val chapter = response.data?.chapter
            if (chapter == null || response.hasErrors()) {
                showErrorMessage(response.errors?.get(0)?.message ?: getString(R.string.error))
                return@launchWhenResumed
            }

            val chapterNumber = chapter.number?.toInt()
            binding.spinner.visibility = View.GONE
            binding.chapterHeader.title =
                if (chapter.number == null) chapter.title else getString(
                    R.string.chapter_title,
                    chapter.number.toString(),
                    chapter.title
                )

            if (chapter.sections.size > 1) {
                val adapter =
                    SectionsAdapter(chapterNumber, chapter.sections, requireContext())
                val layoutManager = LinearLayoutManager(requireContext())
                binding.sections.layoutManager = layoutManager
                val itemDivider = DividerItemDecoration(requireContext(), layoutManager.orientation)
                binding.sections.addItemDecoration(itemDivider)
                binding.sections.adapter = adapter
            } else {
                binding.error.text = getString(R.string.no_section_error)
                binding.error.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorMessage(error: String) {
        binding.spinner.visibility = View.GONE
        binding.error.text = error
        binding.error.visibility = View.VISIBLE
    }
}