package com.example.senior_capstone_budget_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_transactions.*
import kotlinx.android.synthetic.main.fragment_transactions.view.*
import kotlinx.android.synthetic.main.goal_item_view_fragment.*
import kotlinx.android.synthetic.main.transaction_item.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val transactionItemAdapter = GroupAdapter<GroupieViewHolder>()

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //______________Variables For Recycler View____________________

    //here we are adding items to the recycler view using the adapter we created to use images as buttons in a list
    private var displayItems: ArrayList<TransactionItem> = ArrayList()
        set(value) {
            transactionItemAdapter.clear()

            for (sectionItem: TransactionItem in value) {
                val transaction = TransactionAdapter(sectionItem)
                transactionItemAdapter.add(transaction)
            }
            field = value
        }
    //_____________________________________________________________

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValues()
    }

    override fun onResume(){
        super.onResume()
        setValues()
    }

    private fun setValues(){
        transactionRecyclerView.apply {
            transactionRecyclerView.layoutManager = LinearLayoutManager(context)
            transactionRecyclerView.adapter = transactionItemAdapter
        }
        //put functional code here for function calls, etc.
        getTransactionItems()

    }

    fun getTransactionItems() {
        //create home menu items
        val transactionItems = ArrayList<TransactionItem>()

        val size = mT!!.length

        for (i in 0 until size){
            var trans = mT?.getTransaction(i)

            val item = TransactionItem(
                trans!!.paymentTo,
                i, trans!!.amount,
                trans!!.details
            )
            transactionItems.add(item)
        }

        //pass array list to displayItems to pass through Adapter
        displayItems = transactionItems
    }


    fun getSize():Int{
        return displayItems.size
    }
}

class TransactionAdapter(private val item: TransactionItem) : Item() {
    private var itemID = item.id
    private var expanded = false
    private var itemBalance = item.amount.toString()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view
        viewHolder.itemView.transactionName.text = item.title
        viewHolder.itemView.transactionAmount.text = itemBalance
        viewHolder.itemView.transaction_description_textView.text = item.description
        viewHolder.itemView.expand_button.setOnClickListener(View.OnClickListener {
            if (!expanded){
                viewHolder.itemView.expand_button.text = "Collapse Details"
                expanded = true
                viewHolder.itemView.transaction_description_textView.visibility = View.VISIBLE
            }else{
                viewHolder.itemView.expand_button.text = "Expand Details"
                expanded = false
                viewHolder.itemView.transaction_description_textView.visibility = View.GONE
            }

        })

        viewHolder.itemView.delete_transaction_btn.setOnClickListener(View.OnClickListener {
            mT?.removeTransaction(position)
            TransactionsFragment().getTransactionItems()
            transactionItemAdapter.notifyItemRemoved(position)
            transactionItemAdapter.notifyItemRangeChanged(position,TransactionsFragment().getSize())
            mT?.saveTransactions(DashboardActivity().user)
        })
    }

    override fun getLayout(): Int {
        return R.layout.transaction_item
    }
}

data class TransactionItem(var title: String, var id: Int, var amount: Double, var description: String) {

}