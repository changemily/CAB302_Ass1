import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User class
 * This class contains methods that create new users and modify existing users
 * @author - Nickhil Nischal
 * @version - skeleton
 *
 * TO DO: 1) Add in checks for admin user type throughout the User Class: only users with the 'Edit Users' permission
 *           (admins) can give other users the 'Edit Users' permission;
 *        2) Add exceptions
 *        3) Database interface with the User Class
 */
public class User {
    //Variables used for each User
    String Username;
    String Password;
    HashSet<String> Permissions = new HashSet<>();

    //Creates an array with valid User Permissions
    String PermissionsCheckList1[] = {"Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit Users"};

    //Populates a HashSet with permissions from PermissionsCheckList1 array
    HashSet<String> PermissionsCheckList2 = new HashSet<>(Arrays.asList(PermissionsCheckList1));

    /**
     * Method used to check if the permission input by the user is a valid input, it is referencing the
     *  PermissionsCheckList2 HashSet
     * @param inputPermission The input given by the user
     * @return Returns a boolean: true if permission is valid and exists in the PermissionsCheckList2 HashSet, and false
     *  if it is invalid and does not exist in the PermissionsCheckList2 HashSet
     */
    private boolean PermissionChecker(String inputPermission) {
        //Checks if the user has a permission, return true if they do, else false if they do not
        if (PermissionsCheckList2.contains(inputPermission)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Constructor used to create a user. Each user is assigned a username, password, and one permission.
     * @param Username The User's username
     * @param Password The User's password
     * @param Permission1 The User's assigned permission
     */
    User(String Username, String Password, String Permission1) {
        //Checks if the entered permission is a valid permission, else user is not created
        if (PermissionChecker(Permission1) == true) {
            //Assigns the inputted Username, Password, and Permission to the User
            this.Username = Username;
            this.Password = Password;
            this.Permissions.add(Permission1);
            System.out.println("User successfully created.");
        }
        else {
            System.out.println("Invalid Permission Type, user has not been created.");
        }
    }

    /**
     * Overloaded method - assigning two permissions for a new user
     * Constructor used to create a user. Each user is assigned a username, password, and two permissions.
     * @param Username The User's username
     * @param Password The User's password
     * @param Permission1 The User's first assigned permission
     * @param Permission2 The User's second assigned permission
     */
    User(String Username, String Password, String Permission1, String Permission2) {
        //Checks if the entered permissions are valid permissions, else user is not created
        if (PermissionChecker(Permission1) == true && PermissionChecker(Permission2) == true) {
            //Assigns the inputted Username, Password, and Permissions to the User
            this.Username = Username;
            this.Password = Password;
            this.Permissions.add(Permission1);
            this.Permissions.add(Permission2);
            System.out.println("User successfully created.");
        }
        else {
            System.out.println("Invalid Permission Type, user has not been created.");
        }
    }

    /**
     * Overloaded method - assigning three permissions for a new user
     * Constructor used to create a user. Each user is assigned a username, password, and three permissions.
     * @param Username The User's username
     * @param Password The User's password
     * @param Permission1 The User's first assigned permission
     * @param Permission2 The User's second assigned permission
     * @param Permission3 The User's third assigned permission
     */
    User(String Username, String Password, String Permission1, String Permission2, String Permission3) {
        //Checks if the entered permissions are valid permissions, else user is not created
        if (PermissionChecker(Permission1) == true && PermissionChecker(Permission2) == true && PermissionChecker(Permission3) == true) {
            //Assigns the inputted Username, Password, and Permissions to the User
            this.Username = Username;
            this.Password = Password;
            this.Permissions.add(Permission1);
            this.Permissions.add(Permission2);
            this.Permissions.add(Permission3);
            System.out.println("User successfully created.");
        }
        else {
            System.out.println("Invalid Permission Type, user has not been created.");
        }
    }

    /**
     * Overloaded method - assigning four permissions for a new user
     * Constructor used to create a user. Each user is assigned a username, password, and four permissions.
     * @param Username The User's username
     * @param Password The User's password
     * @param Permission1 The User's first assigned permission
     * @param Permission2 The User's second assigned permission
     * @param Permission3 The User's third assigned permission
     * @param Permission4 The User's fourth assigned permission
     */
    User(String Username, String Password, String Permission1, String Permission2, String Permission3, String Permission4) {
        //Checks if the entered permissions are valid permissions, else user is not created
        if (PermissionChecker(Permission1) == true && PermissionChecker(Permission2) == true && PermissionChecker(Permission3) == true && PermissionChecker(Permission4) == true) {
            //Assigns the inputted Username, Password, and Permissions to the User
            this.Username = Username;
            this.Password = Password;
            this.Permissions.add(Permission1);
            this.Permissions.add(Permission2);
            this.Permissions.add(Permission3);
            this.Permissions.add(Permission4);
            System.out.println("User successfully created.");
        }
        else {
            System.out.println("Invalid Permission Type, user has not been created.");
        }
    }

    /**
     * Method used to delete a specified user
     */
    public void DeleteUser() {
        String UsernameTemp = this.Username;
        //Sets specified user's Username, Password, and Permissions to null
        this.Username = null;
        this.Password = null;
        this.Permissions = null;
        System.out.println("The user " + UsernameTemp + " has been successfully deleted.");
    }

    /**
     * Method used to change a specified user's username
     * @param NewUsername The new username which will replace the old username
     */
    public void EditUsername(String NewUsername) {
        String UsernameTemp = this.Username;
        //Replaces old username with newly inputted username
        this.Username = NewUsername;
        System.out.println("Username successfully changed from " + UsernameTemp + " to " + Username + ".");
    }

    /**
     * Method used to change a specified user's password
     * @param NewPassword The new password which will replace the old password
     */
    public void EditPassword(String NewPassword) {
        String PasswordTemp = this.Password;
        //Replaces old password with newly inputted password
        this.Password = NewPassword;
        System.out.println("Password successfully changed from " + PasswordTemp + " to " + Password + ".");
    }

    /**
     * Method used to remove a specified user's permission
     * @param Permission The permission that is to be removed from the specified user's permissions
     */
    public void RemovePermissions(String Permission) {
        //Checks if the entered permission is a valid permission, else permission is not removed from user
        if (PermissionChecker(Permission) == true) {
            //Checks if permission exists in user's HashSet
            if (this.Permissions.contains(Permission)) {
                this.Permissions.remove(Permission);
                System.out.println("Permission " + Permission + " successfully removed.");
            }
        }
        else {
            System.out.println("Invalid Permission Type, permission has not been removed.");
        }
    }

    /**
     * Method used to add a specified user's permission
     * @param Permission The permission that is to be added to the specified user's permissions
     */
    public void AddPermissions(String Permission) {
        //Checks if the entered permission is a valid permission, else permission is not added to user
        if (PermissionChecker(Permission) == true) {
            //Checks if permission already exists in user's HashSet
            if (!this.Permissions.contains(Permission)) {
                this.Permissions.add(Permission);
                System.out.println("Permission " + Permission + " successfully added.");
            }
        }
        else {
            System.out.println("Invalid Permission Type, permission has not been added.");
            //Also need to add in check for user type: only Users with 'Edit Users' permission (Admin) can give other
            // users the 'Edit Users' permission
        }
    }
}
