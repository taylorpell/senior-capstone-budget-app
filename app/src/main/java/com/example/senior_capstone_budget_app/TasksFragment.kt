package com.example.senior_capstone_budget_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.task_item.view.*
import kotlinx.android.synthetic.main.task_item.view.checkBox


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val taskItemAdapter = GroupAdapter<GroupieViewHolder>()

/**
 * A simple [Fragment] subclass.
 * Use the [TasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TasksFragment : Fragment() {

    private var prompt : TextView? = null

    //______________Variables For Recycler View____________________

    //here we are adding items to the recycler view using the adapter we created to use images as buttons in a list
    private var displayItems: ArrayList<TaskItem> = ArrayList()
        set(value) {
            taskItemAdapter.clear()

            for (sectionItem: TaskItem in value) {
                val task = TaskAdapter(sectionItem)
                taskItemAdapter.add(task)
            }
            field = value
        }

    //_____________________________________________________________
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_tasks, container, false)
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
        tasks_recycler_view.apply {
            tasks_recycler_view.layoutManager = LinearLayoutManager(context)
            tasks_recycler_view.adapter = taskItemAdapter
        }
        //put functional code here for function calls, etc.
        getTaskItems()

        taskItemAdapter.setOnItemClickListener { item, _ ->
            taskItemAction(item)
        }

        if (taskItemAdapter.itemCount == 0) {
            prompt?.visibility = View.VISIBLE
        }
    }

    fun getTaskItems() {
        val taskItems = ArrayList<TaskItem>()
        val size = tasks.size

        for (i in 0 until size){
            var task = tasks[i]

            val item = TaskItem(
                task.title,
                i,
                task.isCompleted
            )
            taskItems.add(item)
        }

        //pass array list to displayItems to pass through Adapter
        displayItems = taskItems
    }
    private fun taskItemAction(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>) {
        //handle click action
    }
    fun getSize():Int{
        return displayItems.size
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TasksFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TasksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class TaskAdapter(private val item: TaskItem) : Item() {
    val itemID = item.id
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view, in this case I just want the image
        //viewHolder.itemView.goalImageView.setImageDrawable(item.image)
        viewHolder.itemView.task_name.text = item.title
        if (item.completed) viewHolder.itemView.checkBox.isChecked = true
        if (!item.completed) viewHolder.itemView.checkBox.isChecked = false
        viewHolder.itemView.checkBox.setOnClickListener(View.OnClickListener {
            tasks[position].isCompleted = !tasks[position].isCompleted
            goal?.calculatePercent()
        })
        viewHolder.itemView.task_delete.setOnClickListener(View.OnClickListener {
            goal?.removeTask(position)
            TasksFragment().getTaskItems()
            taskItemAdapter.notifyItemRemoved(position)
            taskItemAdapter.notifyItemRangeChanged(position, TasksFragment().getSize())
            g?.saveGoals(DashboardActivity().user)
        })
    }

    override fun getLayout(): Int {
        return R.layout.task_item
    }
}

data class TaskItem(var title: String, var id: Int, var completed: Boolean)