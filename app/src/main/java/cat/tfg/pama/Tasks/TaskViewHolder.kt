package cat.tfg.pama.Tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R

class TaskViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.task_item, parent, false)) {

    private var mTitleView: TextView? = null

    init {
        mTitleView = itemView.findViewById(R.id.task_title)
    }

    fun bind(task: Task, clickListener: ((Any) -> Unit) ?) {
        mTitleView?.text = task.title
        if(clickListener!=null){
            itemView.setOnClickListener { clickListener(task)}
        }
    }
}