package com.steven.download.download.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.steven.download.download.db.curd.QuerySupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


public class DaoSupport<T> implements IDaoSupport<T> {
    // SQLiteDatabase
    private SQLiteDatabase mSqLiteDatabase;
    // 泛型类
    private Class<T> mClazz;

    private String TAG = "DaoSupport";


    private static final Object[] mPutMethodArgs = new Object[2];

    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();

    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mSqLiteDatabase = sqLiteDatabase;
        this.mClazz = clazz;

        // 创建表
        /*"create table if not exists Person ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "flag boolean)";*/

        StringBuffer sb = new StringBuffer();

        sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");

        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);// 设置权限
            String name = field.getName();
            String type = field.getType().getSimpleName();// int String boolean
            //  type需要进行转换 int --> integer, String text;
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), ")");

        String createTableSql = sb.toString();

        Log.e(TAG, "表语句--> " + createTableSql);

        // 创建表
        mSqLiteDatabase.execSQL(createTableSql);
    }


    // 插入数据库 t 是任意对象
    @Override
    public long insert(T obj) {
        /*ContentValues values = new ContentValues();
        values.put("name",person.getName());
        values.put("age",person.getAge());
        values.put("flag",person.getFlag());
        db.insert("Person",null,values);*/


        // 使用的其实还是  原生的使用方式，只是我们是封装一下而已
        ContentValues values = contentValuesByObj(obj);
        // null  速度比第三方的快一倍左右
        return mSqLiteDatabase.insert(DaoUtil.getTableName(mClazz), null, values);
    }

    @Override
    public void insert(List<T> datas) {
        // 批量插入采用 事物
        mSqLiteDatabase.beginTransaction();
        for (T data : datas) {
            // 调用单条插入
            insert(data);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    private QuerySupport<T> mQuerySupport;

    @Override
    public QuerySupport<T> querySupport() {
        if (mQuerySupport == null) {
            mQuerySupport = new QuerySupport<>(mSqLiteDatabase, mClazz);
        }
        return mQuerySupport;
    }


    // obj 转成 ContentValues
    private ContentValues contentValuesByObj(T obj) {
        // 第三方的 使用比对一下 了解一下源码
        ContentValues values = new ContentValues();
        // 封装values
        Field[] fields = mClazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                // 设置权限，私有和共有都可以访问
                field.setAccessible(true);
                String key = field.getName();
                // 获取value
                Object value = field.get(obj);
                // put 第二个参数是类型  把它转换

                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

                // 方法使用反射 ， 反射在一定程度上会影响性能
                // 源码里面  activity实例 反射  View创建反射

                String filedTypeName = field.getType().getName();
                // 还是使用反射  获取方法  put  缓存方法
                Method putMethod = mPutMethods.get(filedTypeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(filedTypeName, putMethod);
                }

                // 通过反射执行
                putMethod.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }

    /**
     * 删除
     */
    public int delete(String whereClause, String[] whereArgs) {
        return mSqLiteDatabase.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }

    /**
     * 更新  这些你需要对  最原始的写法比较明了 extends
     */
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.update(DaoUtil.getTableName(mClazz),
                values, whereClause, whereArgs);
    }

    // 结合到
    // 1. 网络引擎的缓存
    // 2. 资源加载的源码NDK
}
