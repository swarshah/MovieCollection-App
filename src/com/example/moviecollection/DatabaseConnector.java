// DatabaseConnector.java
// Provides easy connection and creation of UserContacts database.
package com.example.moviecollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector {
	// database name
	private static final String DATABASE_NAME = "MoviesDB";

	private SQLiteDatabase database; // for interacting with the database
	private DatabaseOpenHelper databaseOpenHelper; // creates the database

	// public constructor for DatabaseConnector
	public DatabaseConnector(Context context) {
		// create a new DatabaseOpenHelper
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME,
				null, 1);
	}

	// open the database connection
	public void open() throws SQLException {
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	}

	// close the database connection
	public void close() {
		if (database != null)
			database.close(); // close the database connection
	}

	// inserts a new contact in the database
	public long insertMovie(String title, String year, String director,
			String runtime) {
		ContentValues newMovie = new ContentValues();
		newMovie.put("title", title);
		newMovie.put("year", year);
		newMovie.put("director", director);
		newMovie.put("runtime", runtime);

		open(); // open the database
		long rowID = database.insert("movies", null, newMovie);
		close(); // close the database
		return rowID;
	}

	// updates an existing contact in the database
	public void updateMovie(long id, String title, String year,
			String director, String runtime) {
		ContentValues editMovie = new ContentValues();
		editMovie.put("title", title);
		editMovie.put("year", year);
		editMovie.put("director", director);
		editMovie.put("runtime", runtime);

		open(); // open the database
		database.update("movies", editMovie, "_id=" + id, null);
		close(); // close the database
	} // end method updateContact

	// return a Cursor with all contact names in the database
	public Cursor getAllMovies() {
		return database.query("movies", new String[] { "_id", "title" }, null,
				null, null, null, "title");
	}

	// return a Cursor containing specified contact's information
	public Cursor getOneMovie(long id) {
		return database.query("movies", null, "_id=" + id, null, null, null,
				null);
	}

	// delete the contact specified by the given String name
	public void deleteMovie(long id) {
		open(); // open the database
		database.delete("movies", "_id=" + id, null);
		close(); // close the database
	}

	private class DatabaseOpenHelper extends SQLiteOpenHelper {
		// constructor
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// creates the contacts table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) {
			// query to create a new table named contacts
			String createQuery = "CREATE TABLE movies"
					+ "(_id integer primary key autoincrement,"
					+ "title TEXT, year TEXT, director TEXT, "
					+ "runtime TEXT);";

			db.execSQL(createQuery); // execute query to create the database
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	} // end class DatabaseOpenHelper
} // end class DatabaseConnector

