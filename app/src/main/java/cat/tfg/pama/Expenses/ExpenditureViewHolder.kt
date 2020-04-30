package cat.tfg.pama.Expenses

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R

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
        val price = expenditure.price.toString()+"€"
        mPriceView?.text =  price
        if(clickListener!=null){
            itemView.setOnClickListener { clickListener(expenditure)}
        }
    }

}