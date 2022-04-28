package com.example.giftgiver.features.client.domain.useCases

import android.util.Log
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class GetClientByVkId @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val clientsOffline: ClientsRepOffline,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long) =
        withContext(dispatcher) {
            try {
                clientsRepository.getClientByVkId(vkId)
            } catch (ex: Exception) {
                if (ex is UnknownHostException || (ex is FirebaseFirestoreException && ex.message?.contains(
                        "offline"
                    ) == true)
                ) {
                    Log.e("Internet", ex.toString())
                    clientsOffline.getClientByVkId(vkId)
                } else {
                    null
                }
            }
        }
}
