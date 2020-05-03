package cat.tfg.pama.Nannies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.Expenses.Expenditure
import cat.tfg.pama.R

class NannieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.nannie_item, parent, false)) {

    private var mNameView: TextView? = null
    private var mAgeView: TextView? = null
    private var mRateView: RatingBar? = null

    init {
        mNameView = itemView.findViewById(R.id.nannie_name)
        mAgeView = itemView.findViewById(R.id.nannie_age)
        mRateView = itemView.findViewById(R.id.rating)
    }

    fun bind(nannie: Nannie, clickListener: ((Any) -> Unit) ?) {
        mRateView?.numStars = nannie.stars
        mNameView?.text = nannie.name
        val age = nannie.age.toString()+" años"
        mAgeView?.text =  age
        if(clickListener!=null){
            itemView.setOnClickListener { clickListener(nannie)}
        }
    }
}