package com.cloudhired

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.cloudhired.api.CloudhiredApi
import com.cloudhired.model.ProfessionalProfile
import com.cloudhired.api.Repository
import com.cloudhired.model.ChatRow
import com.cloudhired.model.ProfessionalSummary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application,
    private val state: SavedStateHandle
) : AndroidViewModel(application) {

    private val cloudhiredApi = CloudhiredApi.create()
    private val cloudhiredRepository = Repository(cloudhiredApi)
    private val proSums = MutableLiveData<List<ProfessionalSummary>>()
    private val proProfile = MutableLiveData<ProfessionalProfile>()

    private val appContext = getApplication<Application>().applicationContext
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private var chat = MutableLiveData<List<ChatRow>>()
    private var chatListener: ListenerRegistration? = null
    private val oneFifthWidthPx = (appContext.resources.displayMetrics.widthPixels / 5).toInt()
    val fourFifthWidthPx = 4 * oneFifthWidthPx

    /***
     * Firebase Auth
     * ***/
    fun observeFirebaseAuthLiveData(): LiveData<FirebaseUser?> {
        return firebaseAuthLiveData
    }


    /***
     * FOLLOWING METHODS USED FOR PROFILE
     * ***/
    fun netRefresh() = viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO)
    {
        proSums.postValue(cloudhiredRepository.fetchProSum())
    }

    fun netProfile(username: String) = viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO)
    {
        proProfile.postValue(cloudhiredRepository.fetchProfile(username))
    }

    fun getProfile(): ProfessionalProfile? {
        return proProfile.value
    }

    fun observeProSums(): LiveData<List<ProfessionalSummary>> {
        return proSums
    }

    fun observeProfile(): LiveData<ProfessionalProfile> {
        return proProfile
    }

    /***
     * FOLLOWING METHODS USED FOR CHAT
     * ***/
    fun myUid(): String? {
        return firebaseAuthLiveData.value?.uid
    }
    fun signOut() {
        chatListener?.remove()
        FirebaseAuth.getInstance().signOut()
        chat.value = listOf()
    }

    fun observeChat(): LiveData<List<ChatRow>> {
        return chat
    }
    fun saveChatRow(chatRow: ChatRow) {
        // https://firebase.google.com/docs/firestore/manage-data/add-data#add_a_document
        // Remember to set the rowID of the chatRow before saving it
        chatRow.rowID = db.collection("chMessages").document().id
        db.collection("chMessages")
                .document(chatRow.rowID)
                .set(chatRow)
                .addOnSuccessListener {
                    Log.d( javaClass.simpleName,"Message saved" )
                }
                .addOnFailureListener { e ->
                    Log.d(javaClass.simpleName, "Messaged not saved!!")
                    Log.w(javaClass.simpleName, "Error ", e)
                }
    }

    fun deleteChatRow(chatRow: ChatRow){
        // Delete picture (if any) on the server, asynchronously
        val uuid = chatRow.pictureUUID
//        if(uuid != null) {
//            Storage.deleteImage(uuid)
//        }

        db.collection("chMessages").document(chatRow.rowID).delete()
//        if (uuid != null) {
//            Storage.deleteImage(uuid)
//        }
    }

    fun getChat() {
        if(FirebaseAuth.getInstance().currentUser == null) {
            Log.d(javaClass.simpleName, "Can't get chat, no one is logged in")
            chat.value = listOf()
            return
        }
        // XXX Write me.  Limit total number of chat rows to 100
        db.collection("chMessages")
                .orderBy("timeStamp")
                .limit(100)
                .addSnapshotListener { querySnapshot, ex ->
                    if (ex != null) {
                        return@addSnapshotListener
                    }
                    if (querySnapshot != null) {
                        chat.value = querySnapshot.documents.mapNotNull {
                            it.toObject(ChatRow::class.java)
                        }
                    }
                }
    }

    override fun onCleared() {
        Log.d(javaClass.simpleName, "onCleared!!")
        super.onCleared()
        chatListener?.remove()
    }

}
