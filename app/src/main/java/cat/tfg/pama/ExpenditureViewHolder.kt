package cat.tfg.pama

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenditureViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.expenditure_item, parent, false)) {
    
    private var mTitleView: TextView? = null
    private var mPriceView: TextView? = null

    init {
        mTitleView = itemView.findViewById(R.id.expenditure_title)
        mPriceView = itemView.findViewById(R.id.expenditure_price)
    }

    fun bind(expenditure: Expenditure, clickListener: ((Any) -> Unit) ?) {
        mTitleView?.text = expenditure.title
        mPriceView?.text = expenditure.price.toString()
        if(clickListener!=null){
            itemView.setOnClickListener { clickListener(expenditure)}
        }
    }

}