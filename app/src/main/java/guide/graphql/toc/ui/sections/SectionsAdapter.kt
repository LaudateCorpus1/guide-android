package guide.graphql.toc.ui.sections

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import guide.graphql.toc.R
import guide.graphql.toc.SectionsQuery
import guide.graphql.toc.databinding.SectionBinding

class SectionsAdapter(
    private val context: Context,
    private val chapterNumber: Int,
    private var sections: List<SectionsQuery.Section?> = listOf()
) :
    RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    fun updateSections(sections: List<SectionsQuery.Section?>) {
        this.sections = sections
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return sections.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]
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