package guide.graphql.toc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            binding.spinner.visibility = View.VISIBLE
            binding.error.visibility = View.GONE

            val response = try {
                apolloClient.query(
                    SectionsQuery(id = args.chapterId)
                ).toDeferred().await()
            } catch (e: ApolloException) {
                binding.spinner.visibility = View.GONE
                binding.error.text = "Error making the GraphQL request: ${e.message}"
                binding.error.visibility = View.VISIBLE
                return@launchWhenResumed
            }

            val chapter = response.data?.chapter
            if (chapter == null || response.hasErrors()) {
                binding.spinner.visibility = View.GONE
                binding.error.text = response.errors?.get(0)?.message
                binding.error.visibility = View.VISIBLE
                return@launchWhenResumed
            }

            val chapterNumber = chapter.number?.toInt()
            binding.spinner.visibility = View.GONE
            binding.chapterHeader.title =
                if (chapter.number == null) chapter.title else "Chapter ${chapterNumber}: ${chapter.title}"

            if (chapter.sections.size > 1) {
                val adapter =
                    SectionsAdapter(chapterNumber, chapter.sections as List<SectionsQuery.Section>)
                binding.sections.layoutManager = LinearLayoutManager(requireContext())
                binding.sections.adapter = adapter
            } else {
                binding.error.text = "This chapter has no sections."
                binding.error.visibility = View.VISIBLE
            }
        }
    }
}