package com.example.projetointegrador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "viagens";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_DATA_RETORNO = "data_retorno";
    private static final String COLUMN_DESTINO = "destino";
    private static final String COLUMN_PLACA_VEICULO = "placa_veiculo";
    private static final String COLUMN_KM_TOTAL = "km_total";
    private static final String COLUMN_ABASTECIDO = "abastecido";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITULO + " TEXT, " +
            COLUMN_DATA + " TEXT, " +
            COLUMN_DATA_RETORNO + " TEXT, " +
            COLUMN_DESTINO + " TEXT, " +
            COLUMN_PLACA_VEICULO + " TEXT, " +
            COLUMN_KM_TOTAL + " INTEGER, " +
            COLUMN_ABASTECIDO + " INTEGER);";
    private SQLiteDatabase readableDatabase;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long inserirRegistro(String titulo, String data, String dataRetorno, String destino,
                                String placaVeiculo, int kmTotal, boolean abastecido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_DATA, data);
        values.put(COLUMN_DATA_RETORNO, dataRetorno);
        values.put(COLUMN_DESTINO, destino);
        values.put(COLUMN_PLACA_VEICULO, placaVeiculo);
        values.put(COLUMN_KM_TOTAL, kmTotal);
        values.put(COLUMN_ABASTECIDO, abastecido ? 1 : 0);

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<Viagem> getViagens() {
        List<Viagem> viagens = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Viagem viagem = new Viagem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA_RETORNO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACA_VEICULO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KM_TOTAL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ABASTECIDO)) == 1
                );
                viagens.add(viagem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return viagens;
    }

    public Viagem getViagemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Viagem viagem = new Viagem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA_RETORNO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACA_VEICULO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KM_TOTAL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ABASTECIDO)) == 1
            );
            cursor.close();
            db.close();
            return viagem;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public int deletarViagem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}
