package guide.graphql.toc.ui.sections

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import guide.graphql.toc.R
import guide.graphql.toc.SectionsQuery
import guide.graphql.toc.databinding.SectionBinding

class SectionsAdapter(
    private val context: Context,
    private val chapterNumber: Int
) :
    ListAdapter<SectionsQuery.Section?, SectionsAdapter.ViewHolder>(SectionsDiffCallback()) {

    class ViewHolder(val binding: SectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = getItem(position)
        section?.let {
            holder.binding.sectionTitle.text = context.getString(
                R.string.section_title,
                chapterNumber.toString(),
                section.number.toString(),
                section.title
            )
        }
    }
}

class SectionsDiffCallback : DiffUtil.ItemCallback<SectionsQuery.Section?>() {
    override fun areItemsTheSame(
        oldItem: SectionsQuery.Section,
        newItem: SectionsQuery.Section
    ): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(
        oldItem: SectionsQuery.Section,
        newItem: SectionsQuery.Section
    ): Boolean {
        return oldItem == newItem
    }

}