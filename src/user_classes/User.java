package user_classes;

import java.util.Arrays;
import java.util.HashSet;

/**
 * user_classes.User class
 * This class contains methods that create new users and modify existing users
 * @author - Nickhil Nischal
 * @version - Complete
 */
public class User implements java.io.Serializable {
    // Variables used for each user_classes.User
    public String username;
    public String password;
    public String salt;
    public HashSet<String> permissions = new HashSet<>();

    // Creates an array with valid user_classes.User Permissions
    final String[] PERMISSIONS_CHECK_LIST_1 = {"Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit Users"};

    // Populates a HashSet with permissions from PERMISSIONS_CHECK_LIST_1 array
    HashSet<String> permissionsCheckList2 = new HashSet<>(Arrays.asList(PERMISSIONS_CHECK_LIST_1));

    /**
     * Method used to check if the permission input by the user is a valid input, it is referencing the
     * permissionsCheckList2 HashSet
     * @param inputPermission The input given by the user
     * @return Returns a boolean: true if permission is valid and exists in the PermissionsCheckList2 HashSet, and false
     *  if it is invalid and does not exist in the PermissionsCheckList2 HashSet
     */
    private boolean PermissionChecker(String inputPermission) {
        // Checks if the user has a permission, return true if they do, else false if they do not
        return permissionsCheckList2.contains(inputPermission);
    }

    /**
     * Constructor used to create a new user. Each user is assigned a username, password, and one to four permissions
     * (using variable arguments).
     * @param username The user_classes.User's username
     * @param password The user_classes.User's password
     * @param salt The user_classes.User's specific salt
     * @param permissions The user_classes.User's assigned permission/s
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    public User(String username, String password, String salt, String... permissions) throws Exception {
        // For loop that iterates over each permission that is entered, to check if each permission is valid
        for (String permission: permissions) {
            // Checks if the entered permission is a valid permission, else user is not created
            if (!PermissionChecker(permission)) {
                // Throws new exception
                throw new Exception("Invalid Permission Type, user has not been created.");
            }
        }

        // Assigns the inputted Username, Password, and Permission/s to the user
        this.username = username;
        this.password = password;
        this.salt = salt;
        for (String permission2: permissions) {
            this.permissions.add(permission2);
        }
    }

    /**
     * Method used to delete a specified user
     */
    public void deleteUser() {
        String usernameTemp = this.username;
        // Sets specified user's Username, Password, and Permissions to null
        this.username = null;
        this.password = null;
        this.salt = null;
        this.permissions = null;
    }

    /**
     * Method used to change a specified user's username
     * @param newUsername The new username which will replace the old username
     */
    public void editUsername(String newUsername) {
        // Temporary username, stores current username
        String usernameTemp = this.username;

        // Replaces old username with newly inputted username
        this.username = newUsername;
    }

    /**
     * Method used to change a specified user's password
     * @param newPassword The new password which will replace the old password
     */
    public void editPassword(String newPassword) {
        // Temporary password, stores current username
        String passwordTemp = this.password;

        // Replaces old password with newly inputted password
        this.password = newPassword;
    }

    /**
     * Method used to remove a specified user's permission
     * @param permission The permission that is to be removed from the specified user's permissions
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    public void removePermissions(String permission) throws Exception {
        // Checks if the entered permission is a valid permission, else permission is not removed from user
        if (PermissionChecker(permission)) {
            // Checks if permission exists in user's HashSet
            if (this.permissions.contains(permission)) {
                // Removes permission from permissions hashset
                this.permissions.remove(permission);
            }
        }
        // Else throw an exception
        else {
            // Throws new exception
            throw new Exception("Invalid Permission Type, permission has not been removed.");
        }
    }

    /**
     * Method used to add a specified user's permission
     * @param permission The permission that is to be added to the specified user's permissions
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    public void addPermissions(String permission) throws Exception {
        // Checks if the entered permission is a valid permission, else permission is not added to user
        if (PermissionChecker(permission)) {
            // Checks if permission already exists in user's HashSet
            if (!this.permissions.contains(permission)) {
                // Adds permission to permissions hashset
                this.permissions.add(permission);
            }
        }
        // Else throw an exception
        else {
            // Throws new exception
            throw new Exception("Invalid Permission Type, permission has not been added.");
        }
    }
}
