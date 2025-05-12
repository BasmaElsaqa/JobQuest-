// RemindersFragment 

package com.example.jobquestbottomnavigation.ui.profile

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquestbottomnavigation.R
import com.example.jobquestbottomnavigation.Reminder
import com.example.jobquestbottomnavigation.ReminderReceiver
import com.example.jobquestbottomnavigation.RemindersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class RemindersFragment : Fragment() {

    private lateinit var editTitle: EditText
    private lateinit var editDate: EditText
    private lateinit var btnSave: Button
    private lateinit var remindersRecyclerView: RecyclerView
    private val remindersList = mutableListOf<Reminder>()
    private lateinit var remindersAdapter: RemindersAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminders, container, false)

        editTitle = view.findViewById(R.id.editTitle)
        editDate = view.findViewById(R.id.editDate)
        btnSave = view.findViewById(R.id.btnSave)
        remindersRecyclerView = view.findViewById(R.id.remindersRecyclerView)

        remindersAdapter = RemindersAdapter(remindersList)
        remindersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        remindersRecyclerView.adapter = remindersAdapter

        editDate.setOnClickListener { showDateTimePicker() }
        btnSave.setOnClickListener { saveReminder() }

        loadReminders()
        return view
    }

    private fun showDateTimePicker() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val pickedDate = Calendar.getInstance()
            pickedDate.set(Calendar.YEAR, year)
            pickedDate.set(Calendar.MONTH, month)
            pickedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(requireContext(), { _, hour, minute ->
                pickedDate.set(Calendar.HOUR_OF_DAY, hour)
                pickedDate.set(Calendar.MINUTE, minute)
                pickedDate.set(Calendar.SECOND, 0)

                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                editDate.setText(formatter.format(pickedDate.time))

                // ✅ Correctly update shared calendar object
                calendar.set(
                    pickedDate.get(Calendar.YEAR),
                    pickedDate.get(Calendar.MONTH),
                    pickedDate.get(Calendar.DAY_OF_MONTH),
                    pickedDate.get(Calendar.HOUR_OF_DAY),
                    pickedDate.get(Calendar.MINUTE),
                    pickedDate.get(Calendar.SECOND)
                )
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

        }, currentYear, currentMonth, currentDay).show()
    }


    private fun saveReminder() {
        val title = editTitle.text.toString().trim()
        if (title.isEmpty() || editDate.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter title and date", Toast.LENGTH_SHORT).show()
            return
        }

        var dueTime = calendar.timeInMillis

// Add a buffer of 5 seconds if the selected time is now or earlier
        if (dueTime <= System.currentTimeMillis()) {
            Toast.makeText(requireContext(), "Reminder time must be in the future", Toast.LENGTH_SHORT).show()
            return
        }

        val reminder = Reminder(title, dueTime)

        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .collection("reminders")
            .add(reminder)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Reminder saved", Toast.LENGTH_SHORT).show()

                // ✅ Clear the fields after saving
                editTitle.text.clear()
                editDate.text.clear()

                if (canScheduleExactAlarms()) {
                    scheduleNotification(reminder)
                } else {
                    requestScheduleExactAlarmPermission()
                    Toast.makeText(
                        requireContext(),
                        "Please allow exact alarm permission in settings to enable reminders.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                loadReminders()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save reminder", Toast.LENGTH_SHORT).show()
            }
    }


    private fun loadReminders() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .collection("reminders")
            .orderBy("dueDate")
            .get()
            .addOnSuccessListener { documents ->
                remindersList.clear()
                for (document in documents) {
                    val reminder = document.toObject(Reminder::class.java)
                    remindersList.add(reminder)
                }
                remindersAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load reminders", Toast.LENGTH_SHORT).show()
            }
    }

    private fun scheduleNotification(reminder: Reminder) {
        if (reminder.dueDate > System.currentTimeMillis()) {
            val intent = Intent(requireContext(), ReminderReceiver::class.java)
            intent.putExtra("title", reminder.title)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                reminder.dueDate.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                reminder.dueDate,
                pendingIntent
            )
        } else {
            Toast.makeText(requireContext(), "Cannot schedule reminder in the past", Toast.LENGTH_SHORT).show()
        }

    }

    private fun canScheduleExactAlarms(): Boolean {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun requestScheduleExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:" + requireContext().packageName)
            }
            startActivity(intent)
        }
    }
}