package com.pedromassango.programmers.data;

import com.pedromassango.programmers.data.local.NotificationLocalDataSource;
import com.pedromassango.programmers.data.local.PostsLocalDataSource;
import com.pedromassango.programmers.data.local.UserLocalDataSource;
import com.pedromassango.programmers.data.remote.NotificationRemoteDataSource;
import com.pedromassango.programmers.data.remote.PostsRemoteDataSource;
import com.pedromassango.programmers.data.remote.UserRemoteDataSource;

/**
 * Created by pedromassango on 11/11/17.
 */

public class RepositoryManager {

    private static RepositoryManager INSTANCE = null;

    private RepositoryManager(){

    }

    public static RepositoryManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RepositoryManager();
        }
        return INSTANCE;
    }

    // Prevent multiple instances
    public UsersRepository getUsersRepository(){
        return UsersRepository.getInstance(
                UserRemoteDataSource.getInstance(),
                UserLocalDataSource.getInstance());
    }

    public NotificationRepository getNotificationRepository(){
        return NotificationRepository.getInstance(
                new NotificationRemoteDataSource(),
                new NotificationLocalDataSource());
    }

    public PostsRepository getPostsRepository(){
        return PostsRepository.getInstance(
                PostsRemoteDataSource.getInstance(),
                PostsLocalDataSource.getInstance());
    }
}
