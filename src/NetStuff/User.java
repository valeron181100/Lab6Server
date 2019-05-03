package NetStuff;

import java.io.Serializable;

public class User implements Serializable {
    private String password;
    private String login;
    private boolean isLoggedIn;

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
        return password;
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
        int result = 1;
        int isLogged = isLoggedIn ? 1 : 0;
        result = (int)(result * Math.pow(prime,1) + login.hashCode() * Math.pow(prime,2) +
                password.hashCode() * Math.pow(prime,3));
        return result;
    }
}
