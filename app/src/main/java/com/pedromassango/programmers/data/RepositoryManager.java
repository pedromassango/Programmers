package com.pedromassango.programmers.data;

import com.pedromassango.programmers.data.local.UserLocalDataSource;
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
}
