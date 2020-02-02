package com.twobrothers.overcooked.interactive

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.twobrothers.overcooked.R
import com.twobrothers.overcooked.core.framework.viewModelFactory
import com.twobrothers.overcooked.databinding.FragmentInteractiveStepBinding
import com.twobrothers.overcooked.recipedetails.models.FreeTextIngredient
import com.twobrothers.overcooked.recipedetails.models.InteractiveStep
import com.twobrothers.overcooked.recipedetails.models.QuantifiedIngredient
import com.twobrothers.overcooked.recipedetails.presentation.getQuantifiedIngredientReadableFormat
import kotlinx.android.synthetic.main.fragment_interactive_step.*


class InteractiveStepFragment : Fragment() {

    companion object {

        private const val ARGUMENT_INTERACTIVE_STEP = "InteractiveStepFragment.Argument.Step"
        private const val ARGUMENT_SERVES = "InteractiveStepFragment.Argument.Serves"

        @JvmStatic
        fun newInstance(step: InteractiveStep, serves: Int): InteractiveStepFragment {
            val fragment = InteractiveStepFragment()
            val arguments = Bundle()
            arguments.putSerializable(ARGUMENT_INTERACTIVE_STEP, step)
            arguments.putSerializable(ARGUMENT_SERVES, serves)
            fragment.arguments = arguments
            return fragment
        }

    }

    private lateinit var viewModel: InteractiveStepViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Init ViewModel
        val step = arguments?.get(ARGUMENT_INTERACTIVE_STEP) as InteractiveStep
        val serves = arguments?.getInt(ARGUMENT_SERVES) ?: 0
        viewModel = ViewModelProviders.of(
            this,
            viewModelFactory {
                InteractiveStepViewModel(
                    step,
                    serves
                )
            }
        ).get(InteractiveStepViewModel::class.java)

        // Init Data Binding
        val binding = FragmentInteractiveStepBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Init view model observers
        viewModel.ingredients.observe(this, Observer {
            layout_ingredients.removeAllViews()
            val serves = it.second
            it.first?.map {
                val view = when (it) {
                    is QuantifiedIngredient -> {
                        val view = layoutInflater.inflate(
                            R.layout.view_checkable_ingredient,
                            layout_ingredients,
                            false
                        )
                        view.findViewById<TextView>(R.id.text_description).text =
                            getQuantifiedIngredientReadableFormat(requireContext(), it, serves)
                        view
                    }
                    is FreeTextIngredient -> {
                        val view = layoutInflater.inflate(
                            R.layout.view_checkable_ingredient,
                            layout_ingredients,
                            false
                        )
                        view.findViewById<TextView>(R.id.text_description).text = it.description
                        view
                    }
                    else -> throw throw IllegalStateException("Unable to map interactive ingredient type")
                }
                layout_ingredients.addView(view)
            }
        })

        viewModel.showNotification.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                showNotification(it)
            }
        })

        viewModel.cancelNotification.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                cancelNotification()
            }
        })

        return binding.root
    }

    private fun showNotification(text: String) {
        // TODO: Update notification details
        val intent = InteractiveActivity.newIntent(requireContext(), "6WAwaN0y26rjCpkrhuFn")// Intent(requireContext(), InteractiveActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(requireContext(), "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_access_time)
            .setContentTitle("Interactive Title")
            .setContentText("Timer remaining: $text")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, builder.build())
        }
    }

    private fun cancelNotification() {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }

}