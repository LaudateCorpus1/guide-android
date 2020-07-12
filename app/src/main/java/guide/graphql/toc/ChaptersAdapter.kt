package guide.graphql.toc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import guide.graphql.toc.databinding.ChapterBinding

class ChaptersAdapter(
    private val chapters: List<ChaptersQuery.Chapter>
) :
    RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {

    class ViewHolder(val binding: ChapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    var onItemClicked: ((ChaptersQuery.Chapter) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = chapters[position]
        val header =
            if (chapter.number == null) chapter.title else "Chapter ${chapter.number.toInt()}"

        holder.binding.chapterHeader.text = header
        holder.binding.chapterSubheader.text = if (chapter.number == null) "" else chapter.title

        holder.binding.root.setOnClickListener {
            onItemClicked?.invoke(chapter)
        }
    }
}