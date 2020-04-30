package cat.tfg.pama.Vaccines

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R

class VaccineViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.vaccine_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null

    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
    }

    fun bind(vaccine: Vaccine) {
        mTitleView?.text = vaccine.title
        mYearView?.text = vaccine.date
    }

}