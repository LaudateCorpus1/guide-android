package guide.graphql.toc.ui.chapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFade
import guide.graphql.toc.Status
import guide.graphql.toc.databinding.ChaptersFragmentBinding

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
        val materialFade = MaterialFade()
        exitTransition = materialFade

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.bookHeader)

        val adapter =
            ChaptersAdapter(
                listOf(),
                requireContext()
            ) { chapter ->
                findNavController().navigate(
                    ChaptersFragmentDirections.viewSections(
                        chapterId = chapter.id,
                        chapterNumber = chapter.number?.toInt() ?: -1,
                        chapterTitle = chapter.title
                    )
                )
            }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.chapters.layoutManager = layoutManager
        val itemDivider = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.chapters.addItemDecoration(itemDivider)
        binding.chapters.adapter = adapter

        viewModel.chapterList.observe(viewLifecycleOwner, Observer { chapterListResponse ->
            when (chapterListResponse.status) {
                Status.SUCCESS -> {
                    chapterListResponse.data?.let {
                        adapter.updateChapters(it)
                    }
                }
                Status.ERROR -> Toast.makeText(requireContext(), "Error: ${chapterListResponse.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}