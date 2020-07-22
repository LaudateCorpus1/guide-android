package guide.graphql.toc.ui.sections

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import guide.graphql.toc.R
import guide.graphql.toc.databinding.SectionBinding

class SectionsAdapter(
    private val context: Context,
    private val chapterNumber: Int
) : RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    class ViewHolder(val binding: SectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
}