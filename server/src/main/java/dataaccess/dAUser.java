package dataaccess;

import model.UserData;

public class dAUser implements UserDAO{
    @Override
    public void create(UserData u){
        return;
    }
    @Override
    public UserData get(String username){
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
