package dev.sohair.jsontask.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sohair.jsontask.data.ItemData
import dev.sohair.jsontask.data.PostDto
import dev.sohair.jsontask.data.UserDto
import dev.sohair.jsontask.data.remote.AppNetworking
import dev.sohair.jsontask.data.remote.PlaceholderApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val api: PlaceholderApi = AppNetworking.getApi()

    private val viewModelScope = CoroutineScope(Dispatchers.IO)
    private val TAG = "MainViewModel"

    private val _users = MutableLiveData<List<UserDto>>(emptyList())
    val users: LiveData<List<UserDto>> = _users

    private val _posts = MutableLiveData<List<PostDto>>(emptyList())
    val posts: LiveData<List<PostDto>> = _posts

    val itemDataList: LiveData<List<ItemData>> = MediatorLiveData<List<ItemData>>().apply {
        addSource(users) { userList ->
            val postList = posts.value ?: emptyList()
            value = combineUsersAndPosts(userList, postList)
        }
        addSource(posts) { postList ->
            val userList = users.value ?: emptyList()
            value = combineUsersAndPosts(userList, postList)
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            val result = api.getUsers()

            if (result.isSuccessful) {
                _users.postValue(result.body() ?: emptyList())
            } else {
                Log.d(TAG, "fetchUsers: ${result.errorBody()}")
            }
        }
    }

    fun fetchPosts() {
        viewModelScope.launch {
            val result = api.getPost()

            if (result.isSuccessful) {
                _posts.postValue(result.body() ?: emptyList())
            } else {
                Log.d(TAG, "fetchPosts: ${result.errorBody()}")
            }
        }
    }

    private fun combineUsersAndPosts(users: List<UserDto>, posts: List<PostDto>): List<ItemData> {
        val postMap = posts.associateBy { it.userId } // Create a map of posts by userId
        return users.mapNotNull { user ->
            postMap[user.id]?.let { post -> createItemData(user, post) }
        }
    }

    fun createItemData(user: UserDto, post: PostDto): ItemData {
        return ItemData(
            id = user.id ?: -1,
            latitude = user.address?.geo?.lat ?: "N/A",
            longitude = user.address?.geo?.lng ?:"N/A",
            companyName = user.company?.name ?: "N/A",
            title = post.title ?: "",
            body = post.body?: ""
        )
    }

}