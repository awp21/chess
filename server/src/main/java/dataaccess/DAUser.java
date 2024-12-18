package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Set;

public class DAUser implements UserDAO{
    @Override
    public void create(UserData u){
        userDataBase.add(u);
    }
    @Override
    public UserData get(String username){
        for(UserData user: userDataBase){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        userDataBase = new HashSet<>();
    }

    @Override
    public Set<UserData> getDataBase(){
        return userDataBase;
    }

    private Set<UserData> userDataBase = new HashSet<>();



}
