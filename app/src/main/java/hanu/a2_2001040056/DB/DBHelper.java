package hanu.a2_2001040056.DB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040056.models.Product;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CART = "Cart";
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_NAME  = "name";

    private static final String COLUMN_PRICE = "unitPrice";

    private static final String COLUMN_QUANTITY = "quantity";

    private static final String COLUMN_THUMBNAIL = "thumbnail";

    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE if not exists " + TABLE_CART + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_THUMBNAIL   + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " REAL,"
                    + COLUMN_QUANTITY + " INTEGER"
                    + ");";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
    }
    public List<Product> getAllProductItems() {
        List<Product> cartItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);

        try {
            while (cursor.moveToNext()) {

                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)).replace("'","");
                @SuppressLint("Range") long unitPrice = cursor.getLong(cursor.getColumnIndex(COLUMN_PRICE));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
                @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL)).replace("'","");

                Product product = new Product(id,imageUrl,name,  unitPrice, quantity);
                cartItems.add(product);
            }
        } finally {
            cursor.close();
        }

        return cartItems;
    }
}
