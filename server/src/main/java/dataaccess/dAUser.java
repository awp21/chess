package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Set;

public class dAUser implements UserDAO{
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

    }

    private Set<UserData> userDataBase = new HashSet<>();



}
