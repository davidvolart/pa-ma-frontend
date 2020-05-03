package cat.tfg.pama.Nannies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.nannie_item.view.*

class NannieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.nannie_item, parent, false)) {

    private var mNameView: TextView? = null
    private var mAgeView: TextView? = null
    private var mRateView: RatingBar? = null
    private var mImageView: ImageView? = null

    init {
        mNameView = itemView.findViewById(R.id.nannie_name)
        mAgeView = itemView.findViewById(R.id.nannie_age)
        mRateView = itemView.findViewById(R.id.rating)
        mImageView = itemView.findViewById(R.id.nannie_image)
    }

    fun bind(nannie: Nannie, clickListener: ((Any) -> Unit) ?) {
        Glide.with(itemView)
            .load(nannie.image)
            .into(itemView.nannie_image)
        mRateView?.numStars = nannie.stars
        mNameView?.text = nannie.name
        val age = nannie.age.toString()+" años"
        mAgeView?.text =  age
        if(clickListener!=null){
            itemView.setOnClickListener { clickListener(nannie)}
        }
    }
}