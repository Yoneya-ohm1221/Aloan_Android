package com.example.aloan

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class BorrowerMenuSuccessFragment : Fragment() {
    var back:ImageView?=null
    var recyclerView:RecyclerView?=null
    var borrowerID:String?=null
    var wsipe: SwipeRefreshLayout?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root =inflater.inflate(R.layout.fragment_borrower_menu_success, container, false)
        val sharedPrefer = requireContext().getSharedPreferences(
            LoginBorrowerActivity().appPreference, Context.MODE_PRIVATE)
        borrowerID = sharedPrefer?.getString(LoginBorrowerActivity().borrowerIdPreference, null)
        recyclerView=root.findViewById(R.id.recyclerView)
        back=root.findViewById(R.id.imageviewback)

        back?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment, BorrowerAccountFragment())
            fragmentTransaction.commit()
        }
        wsipe=root.findViewById(R.id.swipe_layout)
        wsipe?.setColorSchemeResources(
                R.color.mainor,
                R.color.mainor,
                R.color.mainor)

        wsipe?.setOnRefreshListener {
            showlist()
            wsipe?.isRefreshing=false
        }

        showlist()
        return root
    }
    private fun showlist() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.BorrowerAllSuccess_url)+borrowerID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data.add(Data(
                                item.getString("Principle"),
                                item.getString("instullment_total"),
                                item.getString("firstname"),
                                item.getString("lastname"),
                                item.getString("imageProfile"),
                                item.getString("date_start"),
                                item.getString("Update_date"),
                                item.getString("Interest")


                            )
                            )
                            recyclerView!!.adapter = DataAdapter(data)

                        }
                    } else {
                        recyclerView!!.adapter = DataAdapter(data)


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                response.code
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    internal class Data(
        var Money: String,var instullment: String,var Firstname: String
        ,var Lastname: String,var imageProfile: String,var dateStart:String,var Update_date:String,var Interest:String

    )
    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_loaner_menu_success,
                parent, false
            )
            return ViewHolder(view)
        }


        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) +
                    getString(R.string.profileBLoaner_image_url) + data.imageProfile
            Picasso.get().load(url).into(holder.imageProfile)
            holder.name.text="????????? ${data.Firstname} ${data.Lastname}"
            holder.money.text="???"+data.Money
            holder.ints.text=data.Interest+"%"
            holder.txtinstall.text=data.instullment
            holder.dateStart.text=data.dateStart
            holder.dateEnd.text=data.Update_date


        }
        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var name : TextView = itemView.findViewById(R.id.txtnameW)
            var money : TextView = itemView.findViewById(R.id.txtmoneyre)
            var txtinstall : TextView = itemView.findViewById(R.id.txtinstall)
            var ints: TextView =itemView.findViewById(R.id.txtints)
            var dateStart: TextView =itemView.findViewById(R.id.txtdatestartg)
            var dateEnd: TextView =itemView.findViewById(R.id.dateendg)
            var con: ConstraintLayout =itemView.findViewById(R.id.consta)
            var imageProfile:ImageView=itemView.findViewById(R.id.imgpro)



        }
    }
}