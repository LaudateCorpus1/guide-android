package guide.graphql.toc.ui.chapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.transition.MaterialSharedAxis
import guide.graphql.toc.R
import guide.graphql.toc.databinding.ChaptersFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ChaptersFragment : Fragment() {

    private val viewModel: ChaptersViewModel by viewModels()

    private lateinit var binding: ChaptersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChaptersFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backward = MaterialSharedAxis(MaterialSharedAxis.X, false)
        reenterTransition = backward

        val forward = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = forward
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity()
        val adapter =
            ChaptersAdapter(
                requireContext()
            ) { chapter ->
                findNavController().navigate(
                    ChaptersFragmentDirections.viewSections(
                        chapterId = chapter.id,
                        chapterNumber = chapter.number?.toInt() ?: -1,
                        chapterTitle = if (chapter.number == null) chapter.title else getString(
                            R.string.chapter_title,
                            chapter.number.toInt().toString(),
                            chapter.title
                        )
                    )
                )
            }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.chapters.layoutManager = layoutManager

        val itemDivider = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.chapters.addItemDecoration(itemDivider)
        binding.chapters.adapter = adapter

        viewModel.chapterList.observe(viewLifecycleOwner, Observer {
            Log.i("ChapterFragment", "Updated chapter list")
            adapter.submitList(it)
        })

        viewModel.chapterException.observe(viewLifecycleOwner, Observer {
            it?.let { exception ->
                Toast.makeText(
                    requireContext(),
                    getString(
                        R.string.graphql_error, if (exception is ApolloException)
                            "GraphQL request failed"
                        else
                            exception.message.orEmpty()

                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}