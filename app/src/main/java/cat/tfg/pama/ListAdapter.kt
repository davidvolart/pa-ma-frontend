package cat.tfg.pama

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val list: MutableList<*>, val clickListener: ((Any) -> Unit) ? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_VACCINE = 0
        const val TYPE_EXPENDITURE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VACCINE -> {
                val inflater = LayoutInflater.from(parent.context)
                VaccineViewHolder(inflater, parent)
            }
            TYPE_EXPENDITURE -> {
                val inflater = LayoutInflater.from(parent.context)
                ExpenditureViewHolder(inflater, parent)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = list[position]
        when (holder) {
            is VaccineViewHolder -> holder.bind(element as Vaccine)
            is ExpenditureViewHolder -> holder.bind(element as Expenditure, clickListener)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = list[position]
        return when (comparable) {
            is Vaccine -> TYPE_VACCINE
            is Expenditure -> TYPE_EXPENDITURE
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    override fun getItemCount(): Int = list.size
}