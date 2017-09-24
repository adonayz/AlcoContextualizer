package edu.wpi.alcoholcontextualizer.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.treemap.TreeModel;
import edu.wpi.alcoholcontextualizer.model.Location;
import edu.wpi.alcoholcontextualizer.model.Person;
import edu.wpi.alcoholcontextualizer.model.Time;
import edu.wpi.alcoholcontextualizer.treemap.AndroidMapItem;

/**
 * Database helper class that creates, inserts, and deletes data and contains helper methods
 * to access the database from other classes.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "drinking_instances";

    // Locations table name
    private static final String TABLE_LOCATIONS = "locations";

    // Locations Table Columns names
    private static final String LOCATION_ID = "lid";
    private static final String LOCATION_NAME = "name";
    private static final String LOCATION_LATITUDE = "latitude";
    private static final String LOCATION_LONGITUDE = "longitude";
    private static final String FREQUENCY_AMOUNT = "frequency_amount";
    private static final String RECENT_DATE = "recent_date";

    // Time table name
    private static final String TABLE_TIMES = "times";

    // Time Table Columns names
    private static final String TIME_ID = "tid";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String DRINK_AMOUNT = "drink_amount";

    // Time table name
    private static final String TABLE_PERSON = "persons";

    // Time Table Columns names
    private static final String PERSON_ID = "pid";
    private static final String PERSON_NAME = "person_name";
    private static final String PERSON_DRINK_AMOUNT = "drink_amy";
    private static final String GROUP_NAME = "group_name";

    private static final String ALL_GROUPS = "All Groups";
    private static final String FAMILY = "Family";
    private static final String FRIENDS = "Friends";
    private static final String COWORKER = "Coworker";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + LOCATION_ID + " INTEGER PRIMARY KEY," + LOCATION_NAME + " TEXT,"
                + LOCATION_LATITUDE + " DOUBLE," + LOCATION_LONGITUDE + " DOUBLE,"
                + FREQUENCY_AMOUNT + " INTEGER," + RECENT_DATE + " TEXT" + ")";

        String CREATE_TIMES_TABLE = "CREATE TABLE " + TABLE_TIMES + "("
                + TIME_ID + " INTEGER PRIMARY KEY," + MONTH + " TEXT,"
                + DAY + " INTEGER," + HOUR + " INTEGER,"
                + DRINK_AMOUNT + " INTEGER" + ")";

        String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_PERSON + "("
                + PERSON_ID + " INTEGER PRIMARY KEY," + PERSON_NAME + " TEXT,"
                + PERSON_DRINK_AMOUNT + " INTEGER," + GROUP_NAME + " TEXT" + ")";

        db.execSQL(CREATE_LOCATIONS_TABLE);
        db.execSQL(CREATE_TIMES_TABLE);
        db.execSQL(CREATE_PEOPLE_TABLE);

        // Location(id, name, lat, long, freqAmt, RecDate)
        db.execSQL("INSERT INTO locations VALUES (1, 'House of Blues', 42.347354, -71.095668, 5, 1453680000 );");
        db.execSQL("INSERT INTO locations VALUES (2, 'The Lower Depths', 42.348605, -71.093864, 2, 1463072230 );");
        db.execSQL("INSERT INTO locations VALUES (3, 'Caskn Flagon', 42.3474, -71.09792, 11, 1462532190 );");
        db.execSQL("INSERT INTO locations VALUES (4, 'Baseball Tavern', 42.344738, -71.096343, 8, 1439285710 );");
        db.execSQL("INSERT INTO locations VALUES (5, 'Boston Beer Works', 42.3645, 71.0603, 2, 1468923140 );");
        db.execSQL("INSERT INTO locations VALUES (6, 'Bleacher Bar', 42.3471, 71.0967, 3, 1462223421 );");
        db.execSQL("INSERT INTO locations VALUES (7, 'Bukowski Tavern', 42.347094, -71.096653, 1, 1469999231 );");
        db.execSQL("INSERT INTO locations VALUES (8, 'Punters Pub', 42.338259, -71.093092, 3, 1467834567 );");

        // Time(id, month, day, hour, drink amount)
        db.execSQL("INSERT INTO times VALUES (1, 'January', 1, 20, 5);");
        db.execSQL("INSERT INTO times VALUES (2, 'January', 2, 10, 3);");

        // Person(id, name, drink amount, group name)
        db.execSQL("INSERT INTO persons VALUES (1, 'Fred', 24, 'Family');");
        db.execSQL("INSERT INTO persons VALUES (2, 'Sarah', 20, 'Family');");
        db.execSQL("INSERT INTO persons VALUES (3, 'George', 8, 'Family');");
        db.execSQL("INSERT INTO persons VALUES (4, 'Katie', 12, 'Coworker');");
        db.execSQL("INSERT INTO persons VALUES (5, 'James', 8, 'Coworker');");
        db.execSQL("INSERT INTO persons VALUES (6, 'Dan', 13, 'Friends');");
        db.execSQL("INSERT INTO persons VALUES (7, 'Josh', 8, 'Friends');");
        db.execSQL("INSERT INTO persons VALUES (8, 'Bob', 11, 'Friends');");
        db.execSQL("INSERT INTO persons VALUES (9, 'Terry', 30, 'Friends');");
        db.execSQL("INSERT INTO persons VALUES (10, 'Mark', 5, 'Coworker');");
        db.execSQL("INSERT INTO persons VALUES (11, 'Joe', 6, 'Family');");
        db.execSQL("INSERT INTO persons VALUES (12, 'John', 2, 'Coworker');");
        db.execSQL("INSERT INTO persons VALUES (13, 'Dave', 4, 'Friends');");
        db.execSQL("INSERT INTO persons VALUES (14, 'Randy', 7, 'Coworker');");
        db.execSQL("INSERT INTO persons VALUES (15, 'Sam', 2, 'Friends');");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);

        // Create tables again
        onCreate(db);
    }

    /*
     *****************************************************************************************
     * CRUD operations for location data
     */

    // Getting single location
    public Location getLocation(int location_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATIONS, new String[]{LOCATION_ID,
                        LOCATION_NAME, LOCATION_LATITUDE, LOCATION_LONGITUDE,
                        FREQUENCY_AMOUNT, RECENT_DATE},
                LOCATION_ID + "=?",
                new String[]{String.valueOf(location_id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Location location = new Location(cursor.getInt(0), cursor.getString(1),
                    cursor.getDouble(2), cursor.getDouble(3),
                    cursor.getInt(4), cursor.getString(5));

            cursor.close();
            return location;
        }

        return null;
    }

    // Getting All Locations
    public List<Location> getAllLocations() {

        List<Location> locationList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setLocationId(cursor.getInt(0));
                location.setLocationName(cursor.getString(1));
                location.setLatitude(cursor.getDouble(2));
                location.setLongitude(cursor.getDouble(3));
                location.setFrequencyAmount(cursor.getInt(4));
                location.setRecentDate(cursor.getString(5));

                // Adding location to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return locationList;
    }

    // Getting All Locations
    public List<Location> getAllLocationsInRecentOrder() {

        List<Location> locationList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS + " ORDER BY 'RECENT_DATE'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setLocationId(cursor.getInt(0));
                location.setLocationName(cursor.getString(1));
                location.setLatitude(cursor.getDouble(2));
                location.setLongitude(cursor.getDouble(3));
                location.setFrequencyAmount(cursor.getInt(4));
                location.setRecentDate(cursor.getString(5));

                // Adding location to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return locationList;
    }

    // Getting Locations Count
    public int getLocationsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Deleting single location
    public void deleteLocation(Location location) {
        //TODO
    }

     /*
      *****************************************************************************************
      * CRUD operations for time data
      */

    // Getting single time entry
    public Time getTime(int time_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TIMES,
                new String[]{TIME_ID, MONTH, DAY, HOUR, DRINK_AMOUNT},
                LOCATION_ID + "=?",
                new String[]{String.valueOf(time_id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            Time time = new Time(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
            cursor.close();
            return time;
        }

        return null;
    }

    // Getting list of time entries with specific date
    public List<Time> getTimeListOnDate(String month, int day) {
        List<Time> timeList = new ArrayList<>();

        System.out.println("Here is the  month " + month);
        System.out.println("Here is the  day " + day);
        String selectQuery = "SELECT  * FROM " + TABLE_TIMES +
                " WHERE MONTH = '" + month + "'" +
                " AND DAY = '" + day + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Time time = new Time();
                time.setTimeId(cursor.getInt(0));
                time.setMonth(cursor.getString(1));
                time.setDay(cursor.getInt(2));
                time.setHour(cursor.getInt(3));
                time.setDrinkAmount(cursor.getInt(4));

                // Adding time to list
                timeList.add(time);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return timeList;
    }

    // Getting All Time entries totals for each month
    public int getMonthTimeTotals(String month) {
        int monthTotal = 0;
        List<Time> timeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  SUM(DRINK_AMOUNT) FROM " + TABLE_TIMES +
                " WHERE MONTH = '" + month + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                monthTotal = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return monthTotal;
    }

    // Getting All Time entries
    public List<Time> getAllTimes() {

        List<Time> timeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TIMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Time time = new Time();
                time.setTimeId(cursor.getInt(0));
                time.setMonth(cursor.getString(1));
                time.setDay(cursor.getInt(2));
                time.setHour(cursor.getInt(3));
                time.setDrinkAmount(cursor.getInt(4));

                // Adding time to list
                timeList.add(time);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return timeList;
    }

    // Getting Times Count
    public int getTimesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TIMES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    // Deleting single time entry
    public void deleteTime(Time time) {
        //TODO
    }

    /*
     *****************************************************************************************
     * CRUD operations for people data
     */

    // Getting single person
    public Person getPerson(int person_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PERSON, new String[]{PERSON_ID,
                        PERSON_NAME, PERSON_DRINK_AMOUNT, GROUP_NAME},
                PERSON_ID + "=?",
                new String[]{String.valueOf(person_id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Person person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
            cursor.close();
            return person;
        }

        return null;
    }

    // Getting All Persons
    public List<Person> getAllPersons() {

        List<Person> personList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setPersonId(cursor.getInt(0));
                person.setPersonName(cursor.getString(1));
                person.setDrinkAmount(cursor.getInt(2));
                person.setGroupName(cursor.getString(3));

                // Adding person to list
                personList.add(person);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return personList;
    }

    // Getting Persons Count
    public int getPersonsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PERSON;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Deleting single person
    public void deletePerson(Person person) {
        //TODO
    }

    /**
     * Creates a map item as a root for a tree model that represents the total
     * amount of drinks for all groups and individual groups
     *
     * @return treemodel with the root item as the total drinks for all groups
     * and children as total for individual groups
     */
    public TreeModel getGroupTreeModel() {
        SQLiteDatabase db = this.getReadableDatabase();

        int allGroupsTotal = getDrinksAmountTotal(db, "");
        int familyTotal = getDrinksAmountTotal(db, FAMILY);
        int friendsTotal = getDrinksAmountTotal(db, FRIENDS);
        int coworkersTotal = getDrinksAmountTotal(db, COWORKER);

        AndroidMapItem rootItem = new AndroidMapItem(allGroupsTotal, ALL_GROUPS);
        TreeModel treeModel = new TreeModel(rootItem);
        treeModel.addChild(new TreeModel(new AndroidMapItem(familyTotal, FAMILY)));
        treeModel.addChild(new TreeModel(new AndroidMapItem(friendsTotal, FRIENDS)));
        treeModel.addChild(new TreeModel(new AndroidMapItem(coworkersTotal, COWORKER)));
        return treeModel;
    }

    /**
     * Queries the database to find the total amount of drinks for all groups and for individual groups
     *
     * @param db        database
     * @param groupName name of the group
     * @return total amount of drinks for groupName
     */
    private int getDrinksAmountTotal(SQLiteDatabase db, String groupName) {
        int total = 0;
        String query = "";

        if (groupName.isEmpty()) {
            query = "SELECT SUM(" + PERSON_DRINK_AMOUNT + ") FROM " + TABLE_PERSON;
        } else {
            query = "SELECT SUM(" + PERSON_DRINK_AMOUNT + ") FROM " + TABLE_PERSON + " WHERE " + GROUP_NAME + "='" + groupName + "'";
        }

        Cursor cursor4 = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor4.moveToFirst()) {
            total = cursor4.getInt(0);
            System.out.println("total: " + total);
        }
        cursor4.close();
        return total;
    }


    public TreeModel getTreeModel(String groupName) {

        SQLiteDatabase db = this.getReadableDatabase();

        AndroidMapItem rootItem = new AndroidMapItem(getDrinksAmountTotal(db, groupName), groupName);
        TreeModel treeModel = new TreeModel(rootItem);

        Cursor cursor = db.query(TABLE_PERSON,
                new String[]{PERSON_NAME, PERSON_DRINK_AMOUNT},
                GROUP_NAME + " =?",
                new String[]{groupName},
                null,
                null,
                null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                int amount = cursor.getInt(1);
                treeModel.addChild(new TreeModel(new AndroidMapItem(amount, name)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return treeModel;
    }


}
