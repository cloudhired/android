package com.cloudhired

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.cloudhired.api.CloudhiredApi
import com.cloudhired.api.Repository
import com.cloudhired.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(application: Application,
    private val state: SavedStateHandle
) : AndroidViewModel(application) {

    private val cloudhiredApi = CloudhiredApi.create()
    private val cloudhiredRepository = Repository(cloudhiredApi)
    private val proSums = MutableLiveData<List<ProfessionalSummary>>()
    private val proProfile = MutableLiveData<ProfessionalProfile>()
    private val myProfile = MutableLiveData<ProfessionalProfile>()
    private val updateError = MutableLiveData<UpdateError>()
    private val notifications = MutableLiveData<List<ChatRow>>()

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
    fun getFirebaseAuthLiveData(): FirebaseUser? {
        return firebaseAuthLiveData.value
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
        proProfile.postValue(cloudhiredRepository.fetchProfile(username, "username"))
    }

    fun netMyProfile(email: String) = viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO)
    {
        myProfile.postValue(cloudhiredRepository.fetchProfile(email, "email"))
    }

    fun updateProfileMV(id: String, data: JsonObject) = viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO
    ) {
        updateError.postValue(cloudhiredRepository.updateMyProfile(id, data))
    }

    fun getMyProfile(): ProfessionalProfile? {
        return myProfile.value
    }

    fun observeUpdateError(): LiveData<UpdateError> {
        return updateError
    }

    fun observeProSums(): LiveData<List<ProfessionalSummary>> {
        return proSums
    }

    fun observeProfile(): LiveData<ProfessionalProfile> {
        return proProfile
    }

    fun observeMyProfile(): LiveData<ProfessionalProfile> {
        return myProfile
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
    fun saveChatRow(toEmail: String, chatRow: ChatRow) {
        // https://firebase.google.com/docs/firestore/manage-data/add-data#add_a_document
        // Remember to set the rowID of the chatRow before saving

        val docRef = db.collection("userInteractions")
            .document(generateConversationId(toEmail, FirebaseAuth.getInstance().currentUser?.email!!))
        docRef.get().addOnSuccessListener {
            if (!it.exists()) {
                val data = hashMapOf<String, List<String>>(
                    "participants" to arrayListOf(toEmail, FirebaseAuth.getInstance().currentUser?.email!!)
                )
                docRef.set(data)
            }
        }

        chatRow.rowID = db.collection("userInteractions").document().id
        db.collection("userInteractions")
            .document(generateConversationId(toEmail, FirebaseAuth.getInstance().currentUser?.email!!))
            .collection("chatMessages")
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

//    fun deleteChatRow(chatRow: ChatRow){
//        // Delete picture (if any) on the server, asynchronously
//        val uuid = chatRow.pictureUUID
//        if(uuid != null) {
//            Storage.deleteImage(uuid)
//        }

//        db.collection("chMessages").document(chatRow.rowID).delete()
//        if (uuid != null) {
//            Storage.deleteImage(uuid)
//        }
//    }

    fun getChat(toEmail: String) {
        if(FirebaseAuth.getInstance().currentUser == null) {
            Log.d(javaClass.simpleName, "Can't get chat, no one is logged in")
            chat.value = listOf()
            return
        }
        // XXX Write me.  Limit total number of chat rows to 100
        db.collection("userInteractions")
            .document(generateConversationId(toEmail, FirebaseAuth.getInstance().currentUser?.email!!))
            .collection("chatMessages")
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

        // following commented code can be used to find all chats related to someone.
//            .whereEqualTo("from", "cloudhired@gmail.com")
//            .whereEqualTo("to", "gaomengen@gmail.com")
//            .get()
//            .addOnSuccessListener { documents ->
//                for ( document in documents) {
//                    document.reference
//                        .collection("chatMessages")
//                        .orderBy("timeStamp")
//                        .limit(100)
//                        .addSnapshotListener { querySnapshot, ex ->
//                            if (ex != null) {
//                                return@addSnapshotListener
//                            }
//                            if (querySnapshot != null) {
//                                chat.value = querySnapshot.documents.mapNotNull {
//                                    it.toObject(ChatRow::class.java)
//                                }
//                            }
//                        }
//                }
//            }


    }

    // email is the owner email. used to get all the conversations owner has
    fun netNotifications(email: String) {
        if(FirebaseAuth.getInstance().currentUser == null) {
            Log.d(javaClass.simpleName, "Can't get chat, no one is logged in")
            chat.value = listOf()
            return
        }
        db.collection("userInteractions")
            .whereArrayContains("participants", email)
            .get()
            .addOnSuccessListener { documents ->
                for ( document in documents) {
                    document.reference
                        .collection("chatMessages")
                        .orderBy("timeStamp", Query.Direction.DESCENDING)
                        .limit(1)
                        .addSnapshotListener { querySnapshot, ex ->
                            if (ex != null) {
                                return@addSnapshotListener
                            }
                            if (querySnapshot != null) {
                                notifications.value = querySnapshot.documents.mapNotNull {
                                    it.toObject(ChatRow::class.java)
                                }
                            }
                        }
                }
            }
    }

    fun observeNotifacations(): LiveData<List<ChatRow>> {
        return notifications
    }

    override fun onCleared() {
        Log.d(javaClass.simpleName, "onCleared!!")
        super.onCleared()
        chatListener?.remove()
    }

    private fun generateConversationId(a: String, b: String): String {
        return if (a.toLowerCase() > b.toLowerCase()) {
            "$b-$a"
        } else {
            "$a-$b"
        }
    }

}
