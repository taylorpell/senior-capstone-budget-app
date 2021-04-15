package com.example.senior_capstone_budget_app


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senior_capstone_budget_app.goals.Goal
import com.example.senior_capstone_budget_app.goals.Goals
import com.example.senior_capstone_budget_app.transaction.MonthlyTransactions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_goals.*
import kotlinx.android.synthetic.main.goal_item.view.*
import kotlinx.android.synthetic.main.transaction_item.view.*
import java.io.IOException
import java.io.InputStream

var g: Goals? = null
var gInput= ""
var index: Int = -1

class GoalsFragment : Fragment() {

    //______________Variables For Recycler View____________________
    private val goalItemAdapter = GroupAdapter<GroupieViewHolder>()

    //here we are adding items to the recycler view using the adapter we created to use images as buttons in a list
    private var displayItems: ArrayList<GoalItem> = ArrayList()
        set(value) {
            goalItemAdapter.clear()

            for (sectionItem: GoalItem in value) {
                val goal = GoalAdapter(sectionItem)
                goalItemAdapter.add(goal)
            }
            field = value
        }
    //_____________________________________________________________

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        g = Goals()

        try{
            val inputStream: InputStream = activity?.applicationContext?.assets!!.open("goals.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            gInput = String(buffer)
        }catch (e: IOException){
            e.printStackTrace()
        }

        g?.loadGoals(gInput)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //items to add with the view, view does not exist at this point

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //view is now created and can be accessed
        super.onViewCreated(view, savedInstanceState)
        //here we will add the recycler view items
        goalsRecyclerView.apply {
            goalsRecyclerView.layoutManager = LinearLayoutManager(context)
            goalsRecyclerView.adapter = goalItemAdapter
        }
        //put functional code here for function calls, etc.
        getGoalItems()

        goalItemAdapter.setOnItemClickListener { item, _ ->
            goalItemAction(item)
        }
        new_goal_button.setOnClickListener {
            findNavController().navigate(R.id.createGoalFragment)
        }
    }

    private fun getGoalItems() {
        val image1: Drawable? =
            context?.let { ResourcesCompat.getDrawable(it.resources,
                R.drawable.ic_button_background, null) }

        //create home menu items
        val goalItems = ArrayList<GoalItem>()
        val item1 = GoalItem(
             g!!.goals[0].title,
            0,
            g!!.goals[0].calculateDays().toString() + " Days Left",
            g!!.goals[0].percent,
            g!!.goals[0].percent.toString() + "% Complete"
        )
        val item2 = GoalItem(
            g!!.goals[1].title,
            1,
            g!!.goals[1].calculateDays().toString() + " Days Left",
            g!!.goals[1].percent,
            g!!.goals[1].percent.toString() + "% Complete"
        )
        val item3 = GoalItem(
            g!!.goals[2].title,
            2,
            g!!.goals[2].calculateDays().toString() + " Days Left",
            g!!.goals[2].percent,
            g!!.goals[2].percent.toString() + "% Complete"
        )


        //add home menu items to an array list
        goalItems.add(item1)
        goalItems.add(item2)
        goalItems.add(item3)


        //pass array list to displayItems to pass through Adapter
        displayItems = goalItems
    }
    fun addGoal(goal : GoalItem) {
        displayItems.add(goal)
    }

    private fun goalItemAction(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>) {
        //handle goal click action
        index = goalItemAdapter.getAdapterPosition(item)
        findNavController().navigate(R.id.goalItemViewFragment)
    }
}

class GoalAdapter(private val item: GoalItem) : Item() {
    val itemID = item.id
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view, in this case I just want the image
        //viewHolder.itemView.goalImageView.setImageDrawable(item.image)
        viewHolder.itemView.goalName.text = item.title
        viewHolder.itemView.daysLeft.text = item.days
        viewHolder.itemView.percentComplete.text = item.percent
    }

    override fun getLayout(): Int {
        return R.layout.goal_item
    }

}


data class GoalItem(var title: String, var id: Int, var days: String, var per: Int, var percent: String)