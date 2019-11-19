package com.leo.structure2019.main.di;

import android.app.Application;

import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.retro.RetrofitRepository;
import com.leo.homeloan.data.room.RoomRepository;
import com.leo.homeloan.data.shared.SharedPrefRepository;

public class Injector {

    private static volatile Repository.RoomRepository roomRepository;
    private static volatile Repository.RemoteRepository remoteRepository;
    private static volatile SharedPrefRepository sharedRepository;

    public static Repository.RoomRepository provideRoomRepository(Application context){
        if(roomRepository == null){
            return new RoomRepository(context);
        }
        return roomRepository;
    }

    public static Repository.RemoteRepository provideRemoteRepository(){
        if(remoteRepository == null){
            return new RetrofitRepository();
        }

        return remoteRepository;
    }

    public static SharedPrefRepository provideSharedRepository(Application context){
        if(sharedRepository == null){
            return new SharedPrefRepository(context);
        }

        return sharedRepository;
    }
}
