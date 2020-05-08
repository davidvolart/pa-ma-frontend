package cat.tfg.pama.Expenses

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R

class ExpenditureViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.expenditure_item, parent, false)) {
    
    private var mTitleView: TextView? = null
    private var mPriceView: TextView? = null
    private var rl_item: RelativeLayout? = null
    private var expenditure_button: ImageButton? = null

    init {
        mTitleView = itemView.findViewById(R.id.expenditure_title)
        mPriceView = itemView.findViewById(R.id.expenditure_price)
        rl_item = itemView.findViewById(R.id.rl_item)
        expenditure_button = itemView.findViewById(R.id.expenditure_button)
    }

    fun bind(expenditure: Expenditure, clickListener: ((Any) -> Unit) ?) {
        rl_item?.setBackgroundColor(Color.parseColor(expenditure.color))
        expenditure_button?.setBackgroundColor(Color.parseColor(expenditure.color))
        mTitleView?.text = expenditure.title
        val price = expenditure.price.toString()+"€"
        mPriceView?.text =  price
        if(clickListener!=null){
            itemView.setOnClickListener { clickListener(expenditure)}
        }
    }

}