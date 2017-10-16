package cmsc355.contactapp;

import android.provider.BaseColumns;

/**
 * Created by Austin on 10/6/2017.
 */

public final class DatabaseContract {
    private DatabaseContract(){}

    public static class Contact implements BaseColumns{
        public static final String TABLE_NAME = "Contact";
        public static final String COLUMN_ID = "Contact_ID";
        public static final String COLUMN_FIRST_NAME = "First_Name";
        public static final String COLUMN_LAST_NAME = "Last_Name";
        public static final String COLUMN_JSON = "JSON";
    }

    public static class Group implements BaseColumns{
        public static final String TABLE_NAME = "Group";
        public static final String COLUMN_ID = "Group_ID";
        public static final String COLUMN_NAME = "Name";
    }

    public static class Relation implements BaseColumns{
        public static final String TABLE_NAME = "Relation";
        public static final String COLUMN_ID = "Relation_ID";
        public static final String COLUMN_CONTACT_ID = "Contact_id";
        public static final String COLUMN_GROUP_ID = "Group_id";
    }

}
