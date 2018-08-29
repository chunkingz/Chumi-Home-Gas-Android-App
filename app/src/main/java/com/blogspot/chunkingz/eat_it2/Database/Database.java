package com.blogspot.chunkingz.eat_it2.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.blogspot.chunkingz.eat_it2.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "GasAppDB.db";
    private static final int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Date", "Time", "CylinderSize", "AmountPayable", "DeliveryMethod", "ExtraComments"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();

        if (c.moveToFirst()){
            do {
                result.add(new Order(c.getString(c.getColumnIndex("Date")),
                                c.getString(c.getColumnIndex("Time")),
                                c.getString(c.getColumnIndex("CylinderSize")),
                                c.getString(c.getColumnIndex("AmountPayable")),
                                c.getString(c.getColumnIndex("DeliveryMethod")),
                                c.getString(c.getColumnIndex("ExtraComments"))
                        ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT into OrderDetail " +
                "(Date, Time, CylinderSize, AmountPayable, DeliveryMethod, ExtraComments) " +
                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s');",
                order.getDate(),
                order.getTime(),
                order.getCylinderSize(),
                order.getAmountPayable(),
                order.getDeliveryMethod(),
                order.getExtraComments()
                );

        db.execSQL(query);
    }
    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM OrderDetail";

        db.execSQL(query);
    }
}
