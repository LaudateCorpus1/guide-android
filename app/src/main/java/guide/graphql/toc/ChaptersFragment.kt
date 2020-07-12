package guide.graphql.toc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import guide.graphql.toc.databinding.ChaptersFragmentBinding

class ChaptersFragment : Fragment() {
    private lateinit var binding: ChaptersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChaptersFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(
                    ChaptersQuery()
                ).toDeferred().await()
            } catch (e: ApolloException) {
                Log.d("ChaptersQuery", "GraphQL request failed", e)
                return@launchWhenResumed
            }

            val chapters = response.data?.chapters
            if (chapters == null || response.hasErrors()) {
                return@launchWhenResumed
            }

            val adapter =
                ChaptersAdapter(chapters)
            binding.chapters.layoutManager = LinearLayoutManager(requireContext())
            binding.chapters.adapter = adapter

            adapter.onItemClicked = { chapter ->
                findNavController().navigate(
                    ChaptersFragmentDirections.viewSections(
                        chapterId = chapter.id
                    )
                )
            }
        }
    }
}