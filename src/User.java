import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User class
 * This class contains methods that create new users and modify existing users
 * @author - Nickhil Nischal
 * @version - under development
 *
 * TO DO: 1) Database interface with the User Class
 */
public class User implements java.io.Serializable {
    // Variables used for each User
    String Username;
    String Password;
    HashSet<String> Permissions = new HashSet<>();

    // Creates an array with valid User Permissions
    String PermissionsCheckList1[] = {"Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit Users"};

    // Populates a HashSet with permissions from PermissionsCheckList1 array
    HashSet<String> PermissionsCheckList2 = new HashSet<>(Arrays.asList(PermissionsCheckList1));

    /**
     * Method used to check if the permission input by the user is a valid input, it is referencing the
     *  PermissionsCheckList2 HashSet
     * @param inputPermission The input given by the user
     * @return Returns a boolean: true if permission is valid and exists in the PermissionsCheckList2 HashSet, and false
     *  if it is invalid and does not exist in the PermissionsCheckList2 HashSet
     */
    private boolean PermissionChecker(String inputPermission) {
        // Checks if the user has a permission, return true if they do, else false if they do not
        return PermissionsCheckList2.contains(inputPermission);
    }

    /**
     * Constructor used to create a new user. Each user is assigned a username, password, and one to four permissions
     * (using variable arguments).
     * @param Username The User's username
     * @param Password The User's password
     * @param Permissions The User's assigned permission/s
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    User(String Username, String Password, String ... Permissions) throws Exception {
        // For loop that iterates over each permission that is entered, to check if each permission is valid
        for (String Permission: Permissions) {
            // Checks if the entered permission is a valid permission, else user is not created
            if (!PermissionChecker(Permission)) {
                throw new Exception("Invalid Permission Type, user has not been created.");
            }
        }

        // Assigns the inputted Username, Password, and Permission/s to the user
        this.Username = Username;
        this.Password = Password;
        for (String Permission2: Permissions) {
            this.Permissions.add(Permission2);
        }
    }

    /**
     * Method used to delete a specified user
     */
    public void DeleteUser() {
        String UsernameTemp = this.Username;
        // Sets specified user's Username, Password, and Permissions to null
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
        // Replaces old username with newly inputted username
        this.Username = NewUsername;
        System.out.println("Username successfully changed from " + UsernameTemp + " to " + Username + ".");
    }

    /**
     * Method used to change a specified user's password
     * @param NewPassword The new password which will replace the old password
     */
    public void EditPassword(String NewPassword) {
        String PasswordTemp = this.Password;
        // Replaces old password with newly inputted password
        this.Password = NewPassword;
        System.out.println("Password successfully changed from " + PasswordTemp + " to " + Password + ".");
    }

    /**
     * Method used to remove a specified user's permission
     * @param Permission The permission that is to be removed from the specified user's permissions
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    public void RemovePermissions(String Permission) throws Exception {
        // Checks if the entered permission is a valid permission, else permission is not removed from user
        if (PermissionChecker(Permission)) {
            // Checks if permission exists in user's HashSet
            if (this.Permissions.contains(Permission)) {
                this.Permissions.remove(Permission);
                System.out.println("Permission " + Permission + " successfully removed.");
            }
        }
        else {
            throw new Exception("Invalid Permission Type, permission has not been removed.");
        }
    }

    /**
     * Method used to add a specified user's permission
     * @param Permission The permission that is to be added to the specified user's permissions
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    public void AddPermissions(String Permission) throws Exception {
        // Checks if the entered permission is a valid permission, else permission is not added to user
        if (PermissionChecker(Permission)) {
            // Checks if permission already exists in user's HashSet
            if (!this.Permissions.contains(Permission)) {
                this.Permissions.add(Permission);
                System.out.println("Permission " + Permission + " successfully added.");
            }
        }
        else {
            throw new Exception("Invalid Permission Type, permission has not been added.");
        }
    }
}
