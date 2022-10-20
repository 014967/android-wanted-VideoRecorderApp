package com.example.data.util

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.FirebaseState
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await
import timber.log.Timber

suspend fun <T> safeSetCall( callFunction: () -> Task<T>) : FirebaseResponse<Nothing> {
    var state = FirebaseState.FAILURE
    callFunction.invoke()
        .addOnSuccessListener {
            state = FirebaseState.SUCCESS
        }
        .addOnFailureListener {
            state = FirebaseState.FAILURE
            Timber.e("safeSetCall: 실패 $it")
        }.await()

    return FirebaseResponse(state = state)
}