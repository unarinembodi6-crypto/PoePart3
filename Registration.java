/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepart3;

/**
 *
 * @author Student
 */
public class Registration {
  protected String userName;
    protected String passWord;
    protected String cellPhone;

    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    public boolean checkCellPhoneNumber(String cellphone) {
        if (cellphone == null) {
            return false;
        }
        return cellphone.matches("^\\+27[0-9]{9}$")
                || cellphone.matches("^0[0-9]{9}$");
    }

    public String registerUser(String username, String password, String cellphone) {
        if (registerStatus(username, password, cellphone)) {
            this.userName = username;
            this.passWord = password;
            this.cellPhone = cellphone;
            return "Registration was a success";
        }
        return "Registration was unsuccessful";
    }

    public boolean registerStatus(String username, String password, String cellphone) {
        return checkUserName(username)
                && checkPasswordComplexity(password)
                && checkCellPhoneNumber(cellphone);
    }
}  

