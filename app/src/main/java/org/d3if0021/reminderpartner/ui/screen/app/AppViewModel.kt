package org.d3if0021.reminderpartner.ui.screen.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.d3if0021.reminderpartners.util.SharedUtil

class AppViewModel : ViewModel(){
    val userFlow = SharedUtil.getUserFlow(viewModelScope)
}