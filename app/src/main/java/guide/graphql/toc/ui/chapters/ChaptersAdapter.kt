package guide.graphql.toc.ui.chapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import guide.graphql.toc.ChaptersQuery
import guide.graphql.toc.R
import guide.graphql.toc.databinding.ChapterBinding

class ChaptersAdapter(
    private val context: Context,
    private val onItemClicked: ((ChaptersQuery.Chapter) -> Unit)
) :
    ListAdapter<ChaptersQuery.Chapter, ChaptersAdapter.ViewHolder>(ChaptersDiffCallback()) {

    class ViewHolder(val binding: ChapterBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = getItem(position)
        val header =
            if (chapter.number == null) chapter.title else context.getString(
                R.string.chapter_number,
                chapter.number.toInt().toString()
            )

        holder.binding.chapterHeader.text = header
        if (chapter.number == null) {
            holder.binding.chapterSubheader.visibility = View.GONE

        } else {
            holder.binding.chapterSubheader.text = chapter.title
            holder.binding.chapterSubheader.visibility = View.VISIBLE
        }

        holder.binding.root.setOnClickListener {
            onItemClicked.invoke(chapter)
        }
    }
}

class ChaptersDiffCallback : DiffUtil.ItemCallback<ChaptersQuery.Chapter>() {
    override fun areItemsTheSame(
        oldItem: ChaptersQuery.Chapter,
        newItem: ChaptersQuery.Chapter
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ChaptersQuery.Chapter,
        newItem: ChaptersQuery.Chapter
    ): Boolean {
       return oldItem == newItem
    }


}