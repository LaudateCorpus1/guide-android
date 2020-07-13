package guide.graphql.toc

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import guide.graphql.toc.databinding.SectionBinding

class SectionsAdapter(
    private val chapterNumber: Int?,
    private val sections: List<SectionsQuery.Section?>,
    private val context: Context
) :
    RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

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
            holder.binding.sectionTitle.text = context.getString(R.string.section_title, chapterNumber.toString(), section.number.toString(), section.title)
        }
    }
}