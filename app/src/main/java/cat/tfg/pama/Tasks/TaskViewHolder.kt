package cat.tfg.pama.Tasks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R

class TaskViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.task_item, parent, false)) {

    private var mTitleView: TextView? = null
    private var rl_item: RelativeLayout? = null
    private var task_title: TextView? = null
    private var task_button: ImageButton? = null

    init {
        mTitleView = itemView.findViewById(R.id.task_title)
        rl_item = itemView.findViewById(R.id.rl_item)
        task_title = itemView.findViewById(R.id.task_title)
        task_button = itemView.findViewById(R.id.task_button)
    }

    fun bind(task: Task, clickListener: ((Any) -> Unit) ?) {

        rl_item?.setBackgroundColor(Color.parseColor(task.color))
        task_button?.setBackgroundColor(Color.parseColor(task.color))

        mTitleView?.text = task.title
        if(clickListener!=null){
            itemView.setOnClickListener { clickListener(task)}
        }
    }
}