package com.sssakib.mobilerechargecloneapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sssakib.fetchthousandscontact.R
import kotlinx.android.synthetic.main.contact_list_items.view.*
import java.util.ArrayList

class ContactAdapter (items : List<Contact>, listener: OnItemClickListener, ctx: Context):RecyclerView.Adapter<ContactAdapter.ViewHolder>(),
    Filterable {

    private lateinit var contactList: List<Contact>
    private lateinit var oldContactList: List<Contact>

    private val listener = listener
    private val ctx = ctx

    inner class ViewHolder(v: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(v){

        var name = v.contactNameTV
        var number = v.contactNumberTV
        var uri = v.contactImage
        fun bind(data: Contact) {
            name.text = data.name
            number.text =  data.number
            if ((data.uri==null)||data.uri.equals("")){
                uri.setImageResource(R.drawable.ic_contact);
            }else {
                Glide.with(ctx)
                    .load(data.uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(uri);
            }
        }

    }
    init {
        contactList = items
        oldContactList = contactList
        notifyDataSetChanged()

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_list_items, parent, false)
        return ViewHolder(inflater, listener)
    }

    override fun getItemCount(): Int {
        return contactList.size
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(contactList[position])
        }
        holder.bind(contactList[position])

    }

//    fun filterList(filteredList: List<Contact>) {
//        contactList = filteredList
//        notifyDataSetChanged()
//    }





    interface OnItemClickListener{
        fun onItemClickListener(contact: Contact)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint.toString().isEmpty()) {
                    contactList = oldContactList
                } else {


                    val resultList = ArrayList<Contact>()
                    for (row in oldContactList) {
                        if (
                            row.number.toString().toLowerCase()
                                ?.contains(constraint.toString().toLowerCase()) or
                            row.name.toString().toLowerCase()
                                ?.contains(constraint.toString().toLowerCase())
                        ) {
                            resultList.add(row)
                        }
                    }
                    contactList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = contactList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contactList = results?.values as ArrayList<Contact>
                notifyDataSetChanged()
            }

        }
    }

}