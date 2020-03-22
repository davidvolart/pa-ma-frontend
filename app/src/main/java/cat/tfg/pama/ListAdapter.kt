package cat.tfg.pama

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val list: List<Vaccine>)
    : RecyclerView.Adapter<VaccineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VaccineViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val vaccine: Vaccine = list[position]
        holder.bind(vaccine)
    }

    override fun getItemCount(): Int = list.size

}