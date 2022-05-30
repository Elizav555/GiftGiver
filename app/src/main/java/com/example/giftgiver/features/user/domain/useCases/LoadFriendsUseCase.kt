package com.example.giftgiver.features.user.domain.useCases

import android.util.Log
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.utils.AppDispatchers
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class LoadFriendsUseCase @Inject constructor(
    private val loadFriendsVKUseCase: LoadFriendsVKUseCase,
    private val clientsRepOffline: ClientsRepOffline,
    private val dispatchers: AppDispatchers
) {
    suspend operator fun invoke(vkId: Long, filter: Boolean = true) = withContext(dispatchers.io) {
        try {
            loadFriendsVKUseCase(vkId, filter)
        } catch (ex: Exception) {
            if (ex is UnknownHostException || (ex is FirebaseFirestoreException && ex.message?.contains(
                    "offline"
                ) == true)
            ) {
                Log.e("load friends", ex.toString())
                clientsRepOffline.getClientByVkId(vkId)?.favFriendsIds?.mapNotNull {
                    clientsRepOffline.getClientByVkId(it)?.info
                } ?: listOf()
            } else {
                listOf()
            }
        }
    }
}
