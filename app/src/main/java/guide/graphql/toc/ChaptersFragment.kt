package guide.graphql.toc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.transition.MaterialSharedAxis
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val backward = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        reenterTransition = backward

        val forward = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = forward
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.bookHeader)

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(
                    ChaptersQuery()
                ).toDeferred().await()
            } catch (e: ApolloException) {
                Log.d("ChaptersQuery", "GraphQL request failed", e)
                return@launchWhenResumed
            }

            if (response.hasErrors()) {
                return@launchWhenResumed
            }

            response.data?.chapters?.let { chapters ->
                val adapter =
                    ChaptersAdapter(chapters) { chapter ->
                        findNavController().navigate(
                            ChaptersFragmentDirections.viewSections(
                                chapterId = chapter.id
                            )
                        )
                    }
                val layoutManager = LinearLayoutManager(requireContext())
                binding.chapters.layoutManager = layoutManager
                val itemDivider = DividerItemDecoration(requireContext(), layoutManager.orientation)
                binding.chapters.addItemDecoration(itemDivider)
                binding.chapters.adapter = adapter
            }

        }
    }
}