

/*      Comments regarding several methods of user manager

        set_user_permissions
        - adds permission to hashset of permissions
        - when editting some permissions with already be there, so don't double up
        * user cant remove edit user from themselves


        set_user_password
        - user must provide username and new password
        - clicks apply, returns acknowledgement
        * if change user password not check for admin, but if not check for it

        delete_user
        - user must put in username of user
        - check for admin priviledgs, and valid username
        * note some billboards will no longer have owners
        * user can't remove themselves

        log_out
        - clicks button to log_out
        - server side stuff that i have no idea how to do

        tests

        set_user_permissions
        - remove a/multiple permission/s
        - add a/multiple permission/s
        - check that user with existing permission doesn't double up on permission
        - check that user with edit user can't remove it from themselves
        - check that user without edit_user permission can't do anything

        set_user_password
        - set own password, with and without admin
        - set other password, with and without admin

        delete_user
        - delete own user, error
        - delete other user, with and without admin

        log_out
        - with and without admin, no issues*/
