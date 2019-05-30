package NetStuff.Net;

import NetStuff.DataBaseWorks.DBConst;
import NetStuff.DataBaseWorks.Hash.StringHasher;
import NetStuff.DataBaseWorks.iQuery;
import mainpkg.Main;

import java.io.Serializable;

public class User implements Serializable, iQuery {
    private String password;
    private String login;
    private String email;
    private boolean isLoggedIn;

    public User(String login, String password, String email){
        this.login = login;
        this.password = password;
        this.email = email;
        isLoggedIn = false;
    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
        isLoggedIn = false;
    }

    public User(){
        isLoggedIn = false;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getPassword() {
        StringHasher stringHasher = new StringHasher("SHA-384");
        String res = stringHasher.getHash(password);
        return res;
    }

    public String getUncryptedPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ПОльзователь " + login;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (!login.equals(other.login))
            return false;
        if (!password.equals(other.password))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 5;
        int result = 18;
        result = result * prime + Main.strHashCode(login) * (int) Math.pow(prime,2);
        result = result * prime + Main.strHashCode(password) * (int) Math.pow(prime,3);
        if(email != null)
            result = result * prime + Main.strHashCode(email) * (int) Math.pow(prime,4);
        return result;
    }

    @Override
    public String getInsertSqlQuery() {
        if(email == null)
            return "INSERT INTO " + DBConst.USERS_TABLE + " VALUES("+this.hashCode() +", '"+ this.getLogin() +"', '"+this.getPassword()+"','EMAIL', 'ADDRESS');";
        else
            return "INSERT INTO " + DBConst.USERS_TABLE + " VALUES("+this.hashCode() +", '"+ this.getLogin() +"', '"+this.getPassword() +"', '"+this.getEmail()+"', 'ADDRESS');";
    }

    @Override
    public String getDelSqlQuery() {
        return "DELETE FROM " + DBConst.USERS_TABLE + " WHERE " + DBConst.TABLES_ID + "=" + this.hashCode() + ";";
    }
}
