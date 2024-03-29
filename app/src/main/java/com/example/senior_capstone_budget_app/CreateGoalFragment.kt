package com.example.senior_capstone_budget_app

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.senior_capstone_budget_app.goals.Goal
import com.example.senior_capstone_budget_app.goals.Tasks
import kotlinx.android.synthetic.main.fragment_create_goal.*
import java.sql.Timestamp
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val newGoal = Goal()
var newGoalTasks: ArrayList<Tasks> = newGoal.goalTasks

/**
 * A simple [Fragment] subclass.
 * Use the [CreateGoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateGoalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var goalAdapter: GoalAdapter? = null
    private var goalsFragment: GoalsFragment? = null
    private lateinit var goalItem: GoalItem
    private var cal: Calendar = Calendar.getInstance()
    private var goalTitle: String = ""
    private var goalDescription: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        cal = Calendar.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        goalsFragment = GoalsFragment()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NewGoalTasksFragment().getNewGoalTaskItems()

        save_goal_button.setOnClickListener {
            Toast.makeText(context, "Goal '${goal_name_edit_text.text}' Has Been Saved", Toast.LENGTH_SHORT).show()
            addGoal()
            g?.saveGoals(DashboardActivity().user)
            findNavController().navigate(R.id.goalsFragment)
        }

        create_goal_calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            hideKeyboard()
            cal.set(year,month,dayOfMonth)
        }
    }

    private fun addGoal(){
        newGoal.title = goal_name_edit_text.text.toString()
        newGoal.description = goal_description.text.toString()
        newGoal.deadline = Date(cal.timeInMillis)

        g?.goals?.add(newGoal)
    }

    private fun hideKeyboard() {
        //hide keyboard on tab selected
        try {
            val inputMethodManager: InputMethodManager = requireActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken, 0
            )

        } catch (error: Exception) {
            //keyboard was not open
            error.printStackTrace()
            return
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateGoalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateGoalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}