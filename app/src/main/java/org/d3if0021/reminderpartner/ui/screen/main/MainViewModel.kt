package org.d3if0021.reminderpartner.ui.screen.main

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import org.d3if0021.reminderpartners.model.Aktivitas
import java.util.Date

class MainViewModel(private val uid: String) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var data = mutableStateListOf<Aktivitas>()
        private set

    var totalTasks = mutableIntStateOf(0)
        private set

    var totalPriorities = mutableIntStateOf(0)
        private set

    // Fungsi untuk mendapatkan total tugas
    private fun getTotalTasks(): Int {
        return data.size
    }

    // Fungsi untuk mendapatkan total prioritas
    private fun getTotalPriorities(): Int {
        return data.count { it.priority == 1 }
    }

    private var registration: ListenerRegistration? = null

    private val listener = EventListener<QuerySnapshot> { value, error ->
        if (error != null) {
            Log.e("MainViewModel", "onEvent:error", error)
            return@EventListener
        }

        value?.documentChanges?.forEach { change ->
            handle(change)
        }
        updateTotals() // Perbarui total setelah setiap perubahan data
    }

    private fun handle(change: DocumentChange) {
        when (change.type) {
            DocumentChange.Type.ADDED -> {
                val aktivitas = change.document.toObject(Aktivitas::class.java).copy(
                    id = change.document.id
                )
                data.add(change.newIndex, aktivitas)
            }
            DocumentChange.Type.MODIFIED -> {
                val aktivitas = change.document.toObject(Aktivitas::class.java).copy(
                    id = change.document.id
                )
                if (change.oldIndex == change.newIndex) {
                    data[change.oldIndex] = aktivitas
                } else {
                    data.removeAt(change.oldIndex)
                    data.add(change.newIndex, aktivitas)
                }
            }
            DocumentChange.Type.REMOVED -> {
                data.removeAt(change.oldIndex)
            }
        }
    }

    init {
        registration = db.collection(Aktivitas.COLLECTION)
            .whereEqualTo(Aktivitas.USER_ID, uid)
            .orderBy(Aktivitas.NAMA)
            .addSnapshotListener(listener)
    }

    fun insert(nama: String, waktu: Date) {
        val timestamp = Timestamp(waktu)
        val aktivitas = Aktivitas(
            userId = uid,
            nama = nama,
            waktu = timestamp,
            tanggal = timestamp,
            checkbox = 0
        )

        db.collection(Aktivitas.COLLECTION)
            .add(aktivitas)
            .addOnSuccessListener { documentReference ->
                Log.d("MainViewModel", "Activity added successfully with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("MainViewModel", "Error adding activity", e)
            }
    }

    fun delete(aktivitas: Aktivitas) {
        if (aktivitas.id.isNotEmpty()) {
            db.collection(Aktivitas.COLLECTION)
                .document(aktivitas.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("MainViewModel", "Activity deleted successfully from Firestore.")
                }
                .addOnFailureListener { e ->
                    Log.e("MainViewModel", "Error deleting activity", e)
                }
        } else {
            Log.e("MainViewModel", "Error: Activity ID is empty!")
        }
    }

    fun updateCheckbox(aktivitas: Aktivitas, isChecked: Boolean) {
        if (aktivitas.id.isNotEmpty()) {
            val updatedCheckbox = if (isChecked) 1 else 0
            db.collection(Aktivitas.COLLECTION)
                .document(aktivitas.id)
                .update(Aktivitas.CHECKBOX, updatedCheckbox)
                .addOnSuccessListener {
                    Log.d("MainViewModel", "Checkbox updated successfully for ID: ${aktivitas.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("MainViewModel", "Error updating checkbox", e)
                }
        } else {
            Log.e("MainViewModel", "Error: Activity ID is empty!")
        }
    }

    fun updatePriority(aktivitas: Aktivitas, isPriority: Boolean) {
        if (aktivitas.id.isNotEmpty()) {
            val updatedPriority = if (isPriority) 1 else 0
            db.collection(Aktivitas.COLLECTION)
                .document(aktivitas.id)
                .update(Aktivitas.PRIORITY, updatedPriority)
                .addOnSuccessListener {
                    Log.d("MainViewModel", "Priority updated successfully for ID: ${aktivitas.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("MainViewModel", "Error updating priority", e)
                }
        } else {
            Log.e("MainViewModel", "Error: Activity ID is empty!")
        }
    }

    fun updateActivity(aktivitas: Aktivitas, nama: String, waktu: Date) {
        if (aktivitas.id.isNotEmpty()) {
            val updatedFields = mapOf(
                Aktivitas.NAMA to nama,
                Aktivitas.WAKTU to Timestamp(waktu),
                Aktivitas.TANGGAL to Timestamp(waktu)
            )

            db.collection(Aktivitas.COLLECTION)
                .document(aktivitas.id)
                .update(updatedFields)
                .addOnSuccessListener {
                    Log.d("MainViewModel", "Activity updated successfully for ID: ${aktivitas.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("MainViewModel", "Error updating activity", e)
                }
        } else {
            Log.e("MainViewModel", "Error: Activity ID is empty!")
        }
    }

    private fun updateTotals() {
        totalTasks.intValue = getTotalTasks()
        totalPriorities.intValue = getTotalPriorities()
    }

    override fun onCleared() {
        registration?.remove()
        registration = null
        super.onCleared()
    }
}