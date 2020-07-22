package guide.graphql.toc.ui.chapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import guide.graphql.toc.databinding.ChapterBinding

class ChaptersAdapter(
    private val context: Context,
    private var chapters: List<String> = listOf(),
    private val onItemClicked: ((String) -> Unit)
) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {

    class ViewHolder(val binding: ChapterBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateChapters(chapters: List<String>) {
        this.chapters = chapters
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = chapters[position]

        holder.binding.chapterHeader.text = chapter

        holder.binding.root.setOnClickListener {
            onItemClicked.invoke(chapter)
        }
    }
}