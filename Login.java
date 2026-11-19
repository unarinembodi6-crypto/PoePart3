/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepart3;
import java.util.Objects;
/**
 *
 * @author Student
 */

   public class Login extends Registration {

    public boolean loginUser(String name, String password) {
        return Objects.equals(name, userName) && Objects.equals(password, passWord);
    }

    public String returnLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            return "Login success";
        } else {
            return "Login failed";
        }
    }
} 

