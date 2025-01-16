package org.d3if0021.reminderpartners.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Aktivitas(
    val id: String = "", // ID dokumen Firestore
    val userId: String = "",
    val nama: String = "",
    val waktu: Timestamp = Timestamp.now(),
    val tanggal: Timestamp = Timestamp.now(),
    val checkbox: Int = 0,
    val priority: Int = 0
) {
    companion object {
        const val COLLECTION = "aktivitas"
        const val USER_ID = "userId"
        const val NAMA = "nama"
        const val WAKTU = "waktu"
        const val TANGGAL = "tanggal"
        const val CHECKBOX = "checkbox"
        const val PRIORITY = "priority"
    }
}